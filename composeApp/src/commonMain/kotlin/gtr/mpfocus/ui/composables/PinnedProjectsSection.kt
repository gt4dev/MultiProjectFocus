package gtr.mpfocus.ui.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.core.UiActions

data class PinnedProjectsSectionState(
    val projects: List<ProjectRowState> = emptyList(),
    val isReorderMode: Boolean = false,
)

sealed interface PinnedProjectsSectionUiActions : UiActions {
    data object ToggleReorderModeClicked : PinnedProjectsSectionUiActions
    data class ProjectRowActions(
        val action: ProjectRowUiActions,
        val pinPosition: Int,
    ) : PinnedProjectsSectionUiActions
}

@Composable
fun PinnedProjectsSection(
    uiState: PinnedProjectsSectionState,
    onAction: (PinnedProjectsSectionUiActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Pinned projects",
        modifier = modifier,
        headerActions = {
            if (uiState.projects.size > 1) {
                OutlinedButton(
                    onClick = { onAction(PinnedProjectsSectionUiActions.ToggleReorderModeClicked) },
                ) {
                    Text(if (uiState.isReorderMode) "Done" else "Reorder")
                }
            }
        },
    ) {
        if (uiState.projects.isEmpty()) {
            EmptySectionText("no pinned projects")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                uiState.projects.forEach { project ->
                    ProjectRow(
                        uiState = project,
                        showPinnedReorderControls = uiState.isReorderMode,
                        onAction = { action ->
                            onAction(
                                PinnedProjectsSectionUiActions.ProjectRowActions(
                                    action = action,
                                    pinPosition = requireNotNull(project.pinPosition),
                                )
                            )
                        },
                    )
                }
            }
        }
    }
}
