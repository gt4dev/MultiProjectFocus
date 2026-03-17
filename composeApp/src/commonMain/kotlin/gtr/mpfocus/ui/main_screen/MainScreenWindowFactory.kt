package gtr.mpfocus.ui.main_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.composables.ScreenHeaderUiActions
import gtr.mpfocus.ui.core.AppWindowSpec
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogViewModelFactory
import gtr.mpfocus.ui.navi.MainNavHost

class MainScreenWindowFactory(
    private val mainScreenViewModelFactory: MainScreenViewModelFactory,
    private val createProjectDialogViewModelFactory: CreateProjectDialogViewModelFactory,
) {
    fun create(initialMessage: MessagePanelState? = null): AppWindowSpec {
        val mainScreenFactory = mainScreenViewModelFactory.create(initialMessage)
        val createProjectDialogFactory = createProjectDialogViewModelFactory.create()
        return AppWindowSpec(
            title = "Multi Project Focus",
            content = {
                MainNavHost(
                    mainScreenViewModelFactory = mainScreenFactory,
                    createProjectDialogViewModelFactory = createProjectDialogFactory,
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
    onCreateProjectDialogOpen: () -> Unit,
) {
    val viewModel: MainScreenViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        onAction = { action ->
            // todo: refactor this [after adding project from context menu]
            //   VM should "call" onCreateProjectDialogOpen
            when (action) {
                is MainScreenUiActions.ScreenHeader ->
                    when (action.action) {
                        ScreenHeaderUiActions.AddProjectClicked -> onCreateProjectDialogOpen()
                    }

                else -> viewModel.onAction(action)
            }
        },
    )
}
