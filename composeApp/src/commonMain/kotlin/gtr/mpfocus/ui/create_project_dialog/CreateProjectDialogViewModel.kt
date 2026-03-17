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
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectDialog.State())
    val uiState: StateFlow<CreateProjectDialog.State> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CreateProjectDialogEffect>()
    private val effects: SharedFlow<CreateProjectDialogEffect> = _effects.asSharedFlow()

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
                    .getOrElse { CreateProjectService.Result.Error("Unable to create project.") }
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
    fun create(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CreateProjectDialogViewModel(
                    folderPicker = folderPicker,
                    createProjectService = createProjectService,
                )
            }
        }
    }
}
