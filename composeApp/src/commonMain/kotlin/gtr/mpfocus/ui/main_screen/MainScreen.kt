package gtr.mpfocus.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.composables.*
import gtr.mpfocus.ui.core.UiActions

object MainScreen {

    data class State(
        val message: MessagePanelState? = null,
        val currentProject: ProjectRowState? = null,
        val pinnedProjects: List<ProjectRowState> = emptyList(),
        val otherProjects: List<ProjectRowState> = emptyList(),
        val isPinnedProjectsReorderMode: Boolean = false,
    )

    sealed interface Actions : UiActions {
        data class ScreenHeader(val action: ScreenHeaderUiActions) : Actions
        data class MessagePanel(val action: MessagePanelUiActions) : Actions
        data class CurrentProjectSection(val action: CurrentProjectSectionUiActions) : Actions
        data class PinnedProjectsSection(val action: PinnedProjectsSectionUiActions) : Actions
        data class OtherProjectsSection(val action: OtherProjectsSectionUiActions) : Actions
    }

}

@Composable
fun MainScreen(
    uiState: MainScreen.State,
    onAction: (MainScreen.Actions) -> Unit,
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
            onAction = { onAction(MainScreen.Actions.ScreenHeader(it)) },
        )

        uiState.message?.let { message ->
            MessagePanel(
                uiState = message,
                onAction = { onAction(MainScreen.Actions.MessagePanel(it)) },
            )
        }

        CurrentProjectSection(
            uiState = CurrentProjectSectionState(project = uiState.currentProject),
            onAction = { onAction(MainScreen.Actions.CurrentProjectSection(it)) },
            modifier = Modifier.testTag(MainScreenTestTags.CURRENT_PROJECT_SECTION),
        )

        PinnedProjectsSection(
            uiState = PinnedProjectsSectionState(
                projects = uiState.pinnedProjects,
                isReorderMode = uiState.isPinnedProjectsReorderMode,
            ),
            onAction = { onAction(MainScreen.Actions.PinnedProjectsSection(it)) },
            modifier = Modifier.testTag(MainScreenTestTags.PINNED_PROJECTS_SECTION),
        )

        OtherProjectsSection(
            uiState = OtherProjectsSectionState(projects = uiState.otherProjects),
            onAction = { onAction(MainScreen.Actions.OtherProjectsSection(it)) },
            modifier = Modifier.testTag(MainScreenTestTags.OTHER_PROJECTS_SECTION),
        )
    }
}

object MainScreenTestTags {
    const val CURRENT_PROJECT_SECTION = "current-project"
    const val PINNED_PROJECTS_SECTION = "pinned-projects"
    const val OTHER_PROJECTS_SECTION = "other-projects"
}

