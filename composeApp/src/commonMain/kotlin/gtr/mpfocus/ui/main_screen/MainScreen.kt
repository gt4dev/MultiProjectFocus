package gtr.mpfocus.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.composables.*
import gtr.mpfocus.ui.core.UiActions

data class MainScreenState(
    val message: MessagePanelState? = null,
    val currentProject: ProjectRowState? = null,
    val pinnedProjects: List<ProjectRowState> = emptyList(),
    val otherProjects: List<ProjectRowState> = emptyList(),
    val isPinnedProjectsReorderMode: Boolean = false,
)

sealed interface MainScreenUiActions : UiActions {
    data class ScreenHeader(val action: ScreenHeaderUiActions) : MainScreenUiActions
    data class MessagePanel(val action: MessagePanelUiActions) : MainScreenUiActions
    data class CurrentProjectSection(val action: CurrentProjectSectionUiActions) : MainScreenUiActions
    data class PinnedProjectsSection(val action: PinnedProjectsSectionUiActions) : MainScreenUiActions
    data class OtherProjectsSection(val action: OtherProjectsSectionUiActions) : MainScreenUiActions
}

@Composable
fun MainScreen(
    uiState: MainScreenState,
    onAction: (MainScreenUiActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ScreenHeader(
            uiState = ScreenHeaderState(),
            onAction = { onAction(MainScreenUiActions.ScreenHeader(it)) },
        )

        uiState.message?.let { message ->
            MessagePanel(
                uiState = message,
                onAction = { onAction(MainScreenUiActions.MessagePanel(it)) },
            )
        }

        CurrentProjectSection(
            uiState = CurrentProjectSectionState(project = uiState.currentProject),
            onAction = { onAction(MainScreenUiActions.CurrentProjectSection(it)) },
        )

        PinnedProjectsSection(
            uiState = PinnedProjectsSectionState(
                projects = uiState.pinnedProjects,
                isReorderMode = uiState.isPinnedProjectsReorderMode,
            ),
            onAction = { onAction(MainScreenUiActions.PinnedProjectsSection(it)) },
        )

        OtherProjectsSection(
            uiState = OtherProjectsSectionState(projects = uiState.otherProjects),
            onAction = { onAction(MainScreenUiActions.OtherProjectsSection(it)) },
        )
    }
}

