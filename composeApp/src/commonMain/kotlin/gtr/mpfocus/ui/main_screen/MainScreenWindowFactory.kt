package gtr.mpfocus.ui.main_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.core.AppWindowSpec

class MainScreenWindowFactory(
    private val mainScreenViewModelFactory: MainScreenViewModelFactory,
) {
    fun create(initialMessage: MessagePanelState? = null): AppWindowSpec {
        val viewModelFactory = mainScreenViewModelFactory.create(initialMessage)
        return AppWindowSpec(
            title = "Multi Project Focus",
            content = {
                MainScreenContainer(viewModelFactory = viewModelFactory)
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
) {
    val viewModel: MainScreenViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}
