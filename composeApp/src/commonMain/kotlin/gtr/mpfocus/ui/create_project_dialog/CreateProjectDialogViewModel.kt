package gtr.mpfocus.ui.create_project_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gtr.mpfocus.domain.model.core.CoreResult
import gtr.mpfocus.domain.model.core.CreateProjectService
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.ui.composables.CreateFolderPanel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private sealed interface CreateProjectDialogEffect {
    data object DismissRequested : CreateProjectDialogEffect
    data object Completed : CreateProjectDialogEffect
}

class CreateProjectDialogViewModel(
    private val folderPicker: FolderPicker,
    private val createProjectService: CreateProjectService,
    private val fileSystemActions: FileSystemActions,
    private val relatedProjectId: Long? = null,
) : ViewModel() {

    internal val _uiState = MutableStateFlow(CreateProjectDialog.State())
    val uiState: StateFlow<CreateProjectDialog.State> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CreateProjectDialogEffect>()
    private val effects: SharedFlow<CreateProjectDialogEffect> = _effects.asSharedFlow()

    init {
        loadRecommendedPath()
    }

    fun onAction(action: CreateProjectDialog.Actions) {
        when (action) {
            CreateProjectDialog.Actions.BrowseClicked -> onBrowseClicked()
            CreateProjectDialog.Actions.CreateClicked -> onCreateClicked()
            CreateProjectDialog.Actions.CloseClicked -> onCloseClicked()
            is CreateProjectDialog.Actions.CreateFolderPanelAction -> onCreateFolderPanelAction(
                action.action
            )

            is CreateProjectDialog.Actions.ProjectPathChanged -> onProjectPathChanged(action.value)
        }
    }

    suspend fun collectEffects(
        onCloseRequest: () -> Unit,
        onCompleted: () -> Unit,
    ) {
        effects.collect { effect ->
            when (effect) {
                CreateProjectDialogEffect.DismissRequested -> onCloseRequest()
                is CreateProjectDialogEffect.Completed -> onCompleted()
            }
        }
    }

    private fun loadRecommendedPath() {
        viewModelScope.launch {
            val recommendationCtx = relatedProjectId
                ?.let(CreateProjectService.RecommendationCtx::ProjectCtx)
                ?: CreateProjectService.RecommendationCtx.GlobalCtx
            val recommendedPath = createProjectService
                .getRecommendedPath(recommendationCtx) ?: return@launch
            _uiState.update { currentState ->
                if (currentState.projectPath.isNotBlank()) {
                    currentState
                } else {
                    currentState.copy(projectPath = recommendedPath.path.toString())
                }
            }
        }
    }

    private fun onProjectPathChanged(value: String) {
        _uiState.update {
            it.copy(
                projectPath = value,
                projectPathError = null,
                createFolderPanel = null,
            )
        }
    }

    private fun onCloseClicked() {
        viewModelScope.launch {
            _effects.emit(CreateProjectDialogEffect.DismissRequested)
        }
    }

    private fun onBrowseClicked() {
        if (uiState.value.isSubmitting
            || uiState.value.createFolderPanel?.status is CreateFolderPanel.Status.InProgress
        ) {
            return
        }

        val initialPath = uiState.value.projectPath.trim().ifBlank { null }
        val selectedPath = folderPicker.pickFolder(initialPath) ?: return

        _uiState.update {
            it.copy(
                projectPath = selectedPath,
                projectPathError = null,
                createFolderPanel = null,
            )
        }
    }

    private fun onCreateClicked() {
        if (uiState.value.isSubmitting || uiState.value.createFolderPanel?.status is CreateFolderPanel.Status.InProgress) {
            return
        }

        val projectPath = uiState.value.projectPath

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    projectPathError = null,
                    createFolderPanel = null,
                )
            }
            when (
                val result = runCatching { createProjectService.createProject(projectPath) }
                    .getOrElse { CoreResult.Error.Message("Unable to add project.") }
            ) {
                CoreResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    _effects.emit(CreateProjectDialogEffect.Completed)
                }

                is CoreResult.Error.FolderDoesNotExist -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            projectPathError = null,
                            createFolderPanel = CreateFolderPanel.State(folderPath = result.path),
                        )
                    }
                }

                is CoreResult.Error.Message -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            projectPathError = result.msg,
                            createFolderPanel = null,
                        )
                    }
                }
            }
        }
    }

    private fun onCreateFolderPanelAction(action: CreateFolderPanel.Actions) {
        when (action) {
            CreateFolderPanel.Actions.CloseClicked -> onCreateFolderPanelCloseClicked()
            CreateFolderPanel.Actions.CreateClicked -> onCreateFolderPanelCreateClicked()
        }
    }

    private fun onCreateFolderPanelCloseClicked() {
        _uiState.update { it.copy(createFolderPanel = null) }
    }

    private fun onCreateFolderPanelCreateClicked() {
        val panelState = uiState.value.createFolderPanel ?: return
        if (panelState.status is CreateFolderPanel.Status.InProgress) {
            return
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    createFolderPanel = currentState.createFolderPanel?.copy(
                        status = CreateFolderPanel.Status.InProgress,
                    ),
                )
            }

            val status = runCatching {
                fileSystemActions.createFolder(panelState.folderPath)
            }.fold(
                onSuccess = { created ->
                    if (created) {
                        CreateFolderPanel.Status.Success
                    } else {
                        CreateFolderPanel.Status.Error("Unable to create folder.")
                    }
                },
                onFailure = { error ->
                    CreateFolderPanel.Status.Error(error.message ?: "Unable to create folder.")
                },
            )

            _uiState.update { currentState ->
                currentState.copy(
                    createFolderPanel = currentState.createFolderPanel?.copy(status = status),
                )
            }
        }
    }
}

class CreateProjectDialogViewModelFactoryProvider(
    private val folderPicker: FolderPicker,
    private val createProjectService: CreateProjectService,
    private val fileSystemActions: FileSystemActions,
) {
    fun createFactory(relatedProjectId: Long?): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CreateProjectDialogViewModel(
                    folderPicker = folderPicker,
                    createProjectService = createProjectService,
                    fileSystemActions = fileSystemActions,
                    relatedProjectId = relatedProjectId,
                )
            }
        }
    }
}
