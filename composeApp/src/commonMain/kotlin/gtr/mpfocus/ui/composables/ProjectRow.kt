package gtr.mpfocus.ui.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gtr.mpfocus.domain.model.core.ProjectFiles
import gtr.mpfocus.ui.core.UiActions

data class ProjectRowState(
    val projectId: Long,
    val pathText: String,
    val selectedFile: ProjectFiles = ProjectFiles.File1,
    val availableFiles: List<ProjectFiles> = ProjectFiles.entries,
    val isPinned: Boolean = false,
    val canSetAsCurrent: Boolean = true,
    val canMovePinnedUp: Boolean = false,
    val canMovePinnedDown: Boolean = false,
)

val ProjectRowState.pinActionLabel: String
    get() = if (isPinned) "Unpin" else "Pin"

sealed interface ProjectRowUiActions : UiActions {
    data class SetCurrentClicked(val projectId: Long) : ProjectRowUiActions
    data class OpenFolderClicked(val projectId: Long) : ProjectRowUiActions
    data class OpenFileClicked(
        val projectId: Long,
        val file: ProjectFiles,
    ) : ProjectRowUiActions

    data class TogglePinnedClicked(val projectId: Long) : ProjectRowUiActions
    data class MovePinnedUpClicked(val projectId: Long) : ProjectRowUiActions
    data class MovePinnedDownClicked(val projectId: Long) : ProjectRowUiActions
    data class FileSelected(
        val projectId: Long,
        val file: ProjectFiles,
    ) : ProjectRowUiActions

    data class DeleteClicked(val projectId: Long) : ProjectRowUiActions
    data class AddSubProjectClicked(val projectId: Long) : ProjectRowUiActions
}

@Composable
fun ProjectRow(
    uiState: ProjectRowState,
    showPinnedReorderControls: Boolean,
    onAction: (ProjectRowUiActions) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(ProjectRowTestTags.row(uiState.projectId)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    modifier = Modifier.testTag(ProjectRowTestTags.buttonSetCurrent(uiState.projectId)),
                    onClick = { onAction(ProjectRowUiActions.SetCurrentClicked(uiState.projectId)) },
                    enabled = uiState.canSetAsCurrent,
                ) {
                    Text("Set current")
                }
                TextButton(onClick = { onAction(ProjectRowUiActions.OpenFolderClicked(uiState.projectId)) }) {
                    Text("Open folder")
                }
                TextButton(
                    onClick = {
                        onAction(
                            ProjectRowUiActions.OpenFileClicked(
                                projectId = uiState.projectId,
                                file = uiState.selectedFile,
                            )
                        )
                    },
                ) {
                    Text("Open file")
                }
                TextButton(onClick = { onAction(ProjectRowUiActions.TogglePinnedClicked(uiState.projectId)) }) {
                    Text(uiState.pinActionLabel)
                }
                ProjectFileSelector(
                    uiState = ProjectFileSelectorState(
                        selectedFile = uiState.selectedFile,
                        availableFiles = uiState.availableFiles,
                    ),
                    onAction = { action ->
                        when (action) {
                            is ProjectFileSelectorUiActions.FileSelected -> {
                                onAction(
                                    ProjectRowUiActions.FileSelected(
                                        projectId = uiState.projectId,
                                        file = action.file,
                                    )
                                )
                            }
                        }
                    },
                )
                ProjectContextMenu(
                    uiState = ProjectContextMenuState(projectId = uiState.projectId),
                    onAction = { action ->
                        when (action) {
                            ProjectContextMenuUiActions.AddSubProjectClicked -> {
                                onAction(ProjectRowUiActions.AddSubProjectClicked(uiState.projectId))
                            }

                            ProjectContextMenuUiActions.DeleteClicked -> {
                                onAction(ProjectRowUiActions.DeleteClicked(uiState.projectId))
                            }
                        }
                    },
                )
                if (showPinnedReorderControls) {
                    TextButton(
                        onClick = { onAction(ProjectRowUiActions.MovePinnedUpClicked(uiState.projectId)) },
                        enabled = uiState.canMovePinnedUp,
                    ) {
                        Text("Up")
                    }
                    TextButton(
                        onClick = { onAction(ProjectRowUiActions.MovePinnedDownClicked(uiState.projectId)) },
                        enabled = uiState.canMovePinnedDown,
                    ) {
                        Text("Down")
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(ProjectRowTestTags.path(uiState.projectId))
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                Text(
                    text = uiState.pathText,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

object ProjectRowTestTags {
    fun row(projectId: Long): String = "project-row-$projectId"
    fun path(projectId: Long): String = "project-row-$projectId-path"
    fun buttonSetCurrent(projectId: Long): String = "project-row-$projectId-set-current"
}
