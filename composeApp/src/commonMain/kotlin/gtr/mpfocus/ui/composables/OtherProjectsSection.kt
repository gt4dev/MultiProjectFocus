package gtr.mpfocus.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.core.UiActions

data class OtherProjectsSectionState(
    val projects: List<ProjectRowState> = emptyList(),
)

sealed interface OtherProjectsSectionUiActions : UiActions {
    data class ProjectRowActions(val action: ProjectRowUiActions) : OtherProjectsSectionUiActions
}

@Composable
fun OtherProjectsSection(
    uiState: OtherProjectsSectionState,
    onAction: (OtherProjectsSectionUiActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Other projects",
        modifier = modifier,
    ) {
        if (uiState.projects.isEmpty()) {
            EmptySectionText("no other projects")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                uiState.projects.forEach { project ->
                    ProjectRow(
                        uiState = project,
                        showPinnedReorderControls = false,
                        onAction = { action -> onAction(OtherProjectsSectionUiActions.ProjectRowActions(action)) },
                    )
                }
            }
        }
    }
}
