package gtr.mpfocus.ui.create_project_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateProjectDialogContainer(
    viewModelFactory: ViewModelProvider.Factory,
    onCloseRequest: () -> Unit,
    onCompleted: () -> Unit,
) {
    val viewModel: CreateProjectDialogViewModel = viewModel(
        key = "create-project-dialog", // todo: not needed
        factory = viewModelFactory,
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.collectEffects(
            onCloseRequest = onCloseRequest,
            onCompleted = onCompleted,
        )
    }

    CreateProjectDialog(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}