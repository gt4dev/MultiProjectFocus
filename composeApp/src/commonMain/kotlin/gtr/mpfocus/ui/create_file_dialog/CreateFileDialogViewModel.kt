package gtr.mpfocus.ui.create_file_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.OperatingSystemActions
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private sealed interface CreateFileDialogEffect {
    data object DismissRequested : CreateFileDialogEffect
    data object Completed : CreateFileDialogEffect
}

class CreateFileDialogViewModel(
    private val startParameters: CreateFileDialog.StartParameters,
    private val fileSystemActions: FileSystemActions,
    private val operatingSystemActions: OperatingSystemActions,
) : ViewModel() {

    internal val _uiState = MutableStateFlow(
        CreateFileDialog.State(
            topMessage = startParameters.extraInfo,
            fileName = startParameters.file.fileName,
            folderPath = startParameters.file.folderPath.path.toString(),
        )
    )
    val uiState: StateFlow<CreateFileDialog.State> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CreateFileDialogEffect>()
    private val effects: SharedFlow<CreateFileDialogEffect> = _effects.asSharedFlow()

    fun onAction(action: CreateFileDialog.Actions) {
        when (action) {
            CreateFileDialog.Actions.CancelClicked -> onCancelClicked()
            CreateFileDialog.Actions.CloseErrorClicked -> onCloseErrorClicked()
            CreateFileDialog.Actions.CreateFileClicked -> onCreateFileClicked()
            CreateFileDialog.Actions.OpenFolderClicked -> onOpenFolderClicked()
        }
    }

    suspend fun collectEffects(
        onCloseRequest: () -> Unit,
        onCompleted: () -> Unit,
    ) {
        effects.collect { effect ->
            when (effect) {
                CreateFileDialogEffect.DismissRequested -> onCloseRequest()
                CreateFileDialogEffect.Completed -> onCompleted()
            }
        }
    }

    private fun onCancelClicked() {
        viewModelScope.launch {
            _effects.emit(CreateFileDialogEffect.DismissRequested)
        }
    }

    private fun onCloseErrorClicked() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun onCreateFileClicked() {
        if (uiState.value.isCreating) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isCreating = true,
                    errorMessage = null,
                )
            }

            val result = runCatching {
                fileSystemActions.createFile(startParameters.file)
            }

            result.fold(
                onSuccess = { created ->
                    if (created) {
                        _effects.emit(CreateFileDialogEffect.Completed)
                    } else {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                errorMessage = "Unable to create file.",
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = error.message ?: "Unable to create file.",
                        )
                    }
                },
            )
        }
    }

    private fun onOpenFolderClicked() {
        viewModelScope.launch {
            operatingSystemActions.openFolder(startParameters.file.folderPath)
        }
    }
}


class CreateFileDialogViewModelFactoryProvider(
    private val fileSystemActions: FileSystemActions,
    private val operatingSystemActions: OperatingSystemActions,
) {
    fun createFactory(startParameters: CreateFileDialog.StartParameters): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                CreateFileDialogViewModel(
                    startParameters = startParameters,
                    fileSystemActions = fileSystemActions,
                    operatingSystemActions = operatingSystemActions,
                )
            }
        }
    }
}
