package gtr.mpfocus.ui.main_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.core.AppWindowSpec
import gtr.mpfocus.ui.create_file_dialog.CreateFileDialog
import gtr.mpfocus.ui.create_file_dialog.CreateFileDialogViewModelFactoryProvider
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogViewModelFactoryProvider
import gtr.mpfocus.ui.navi.MainNavHost

class MainScreenWindowFactory(
    private val mainScreenViewModelFactoryProvider: MainScreenViewModelFactoryProvider,
    private val createProjectDialogViewModelFactoryProvider: CreateProjectDialogViewModelFactoryProvider,
    private val createFileDialogViewModelFactoryProvider: CreateFileDialogViewModelFactoryProvider,
) {
    fun create(initialMessage: MessagePanelState? = null): AppWindowSpec {
        return AppWindowSpec(
            title = "Multi Project Focus",
            content = {
                MainNavHost(
                    mainScreenViewModelFactoryProvider = mainScreenViewModelFactoryProvider,
                    initialMessage = initialMessage,
                    createProjectDialogViewModelFactoryProvider = createProjectDialogViewModelFactoryProvider,
                    createFileDialogViewModelFactoryProvider = createFileDialogViewModelFactoryProvider,
                )
            },
        )
    }
}


/**
 * function binds `pure composable screen` with its ViewModel
 */
@Composable
fun MainScreenContainer(
    viewModelFactory: ViewModelProvider.Factory,
    onCreateProjectDialogOpen: (Long?) -> Unit,
    onCreateFileDialogOpen: (CreateFileDialog.StartParameters) -> Unit,
) {
    val viewModel: MainScreenViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MainScreenEffect.CreateProjectDialogRequested ->
                    onCreateProjectDialogOpen(effect.relatedProjectId)
                is MainScreenEffect.CreateFileDialogRequested ->
                    onCreateFileDialogOpen(effect.startParameters)
            }
        }
    }

    MainScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}
