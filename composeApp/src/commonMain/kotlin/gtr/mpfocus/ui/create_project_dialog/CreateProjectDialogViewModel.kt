package gtr.mpfocus.ui.create_project_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gtr.mpfocus.domain.model.core.CreateProjectService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private sealed interface CreateProjectDialogEffect {
    data object DismissRequested : CreateProjectDialogEffect
    data object Completed : CreateProjectDialogEffect
}

class CreateProjectDialogViewModel(
    private val folderPicker: FolderPicker,
    private val createProjectService: CreateProjectService,
    private val relatedProjectId: Long? = null,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectDialog.State())
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
            val recomCtx = relatedProjectId
                ?.let(CreateProjectService.RecomCtx::ProjectCtx)
                ?: CreateProjectService.RecomCtx.GlobalCtx
            val recommendedPath = createProjectService
                .getRecommendedPath(recomCtx) ?: return@launch
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
            )
        }
    }

    private fun onCloseClicked() {
        viewModelScope.launch {
            _effects.emit(CreateProjectDialogEffect.DismissRequested)
        }
    }

    private fun onBrowseClicked() {
        val initialPath = uiState.value.projectPath.trim().ifBlank { null }
        val selectedPath = folderPicker.pickFolder(initialPath) ?: return

        _uiState.update {
            it.copy(
                projectPath = selectedPath,
                projectPathError = null,
            )
        }
    }

    private fun onCreateClicked() {
        val projectPath = uiState.value.projectPath

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, projectPathError = null) }
            when (
                val result = runCatching { createProjectService.createProject(projectPath) }
                    .getOrElse { CreateProjectService.Result.Error("Unable to add project.") }
            ) {
                CreateProjectService.Result.Success -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    _effects.emit(CreateProjectDialogEffect.Completed)
                }

                is CreateProjectService.Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            projectPathError = result.message,
                        )
                    }
                }
            }
        }
    }
}

class CreateProjectDialogViewModelFactory(
    private val folderPicker: FolderPicker,
    private val createProjectService: CreateProjectService,
) {
    fun create(relatedProjectId: Long?): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CreateProjectDialogViewModel(
                    folderPicker = folderPicker,
                    createProjectService = createProjectService,
                    relatedProjectId = relatedProjectId,
                )
            }
        }
    }
}
