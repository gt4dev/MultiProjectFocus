package gtr.mpfocus.ui.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gtr.mpfocus.ui.core.UiActions

data class CurrentProjectSectionState(
    val project: ProjectRowState? = null,
)

sealed interface CurrentProjectSectionUiActions : UiActions {
    data object UnsetCurrentProjectClicked : CurrentProjectSectionUiActions
    data class ProjectRowActions(val action: ProjectRowUiActions) :
        CurrentProjectSectionUiActions
}

@Composable
fun CurrentProjectSection(
    uiState: CurrentProjectSectionState,
    onAction: (CurrentProjectSectionUiActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Current project",
        modifier = modifier,
        headerActions = {
            if (uiState.project != null) {
                TextButton(
                    onClick = { onAction(CurrentProjectSectionUiActions.UnsetCurrentProjectClicked) },
                ) {
                    Text("Unset current")
                }
            }
        },
    ) {
        if (uiState.project == null) {
            EmptySectionText("no current project")
        } else {
            ProjectRow(
                uiState = uiState.project,
                showPinnedReorderControls = false,
                onAction = { action -> onAction(CurrentProjectSectionUiActions.ProjectRowActions(action)) },
            )
        }
    }
}
