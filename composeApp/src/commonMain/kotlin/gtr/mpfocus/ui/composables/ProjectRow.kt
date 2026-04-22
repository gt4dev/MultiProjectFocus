package gtr.mpfocus.ui.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.domain.model.read.FileName
import gtr.mpfocus.ui.core.UiActions


object ProjectRow {

    data class State(
        val projectId: Long,
        val pathText: String,
        val selectedFile: ProjectFile,
        val availableFiles: List<FileName>,
        val pinPosition: Int? = null,
        val canSetAsCurrent: Boolean = true,
        val canMovePinnedUp: Boolean = false,
        val canMovePinnedDown: Boolean = false,
    ) {
        val isPinned: Boolean
            get() = (pinPosition != null)
    }

    sealed interface Actions : UiActions {
        data class SetCurrentClicked(val projectId: Long) : Actions
        data class OpenFolderClicked(val projectId: Long) : Actions
        data class OpenFileClicked(
            val projectId: Long,
            val file: ProjectFile,
        ) : Actions

        data class TogglePinnedClicked(val projectId: Long) : Actions
        data class MovePinnedUpClicked(val projectId: Long) : Actions
        data class MovePinnedDownClicked(val projectId: Long) : Actions
        data class FileSelected(
            val projectId: Long,
            val file: ProjectFile,
        ) : Actions

        data class DeleteClicked(val projectId: Long) : Actions
        data class AddProjectClicked(val relatedProjectId: Long) : Actions
    }
}

@Composable
fun ProjectRow(
    uiState: ProjectRow.State,
    showPinnedReorderControls: Boolean,
    onAction: (ProjectRow.Actions) -> Unit,
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
                OutlinedButton(
                    modifier = Modifier.testTag(ProjectRowTestTags.buttonSetCurrent(uiState.projectId)),
                    onClick = { onAction(ProjectRow.Actions.SetCurrentClicked(uiState.projectId)) },
                    enabled = uiState.canSetAsCurrent,
                ) {
                    Text("Set current")
                }
                OutlinedButton(onClick = { onAction(ProjectRow.Actions.OpenFolderClicked(uiState.projectId)) }) {
                    Text("Open folder")
                }

                OpenFileSplitButton(
                    projectId = uiState.projectId,
                    initialSelectedFile = uiState.selectedFile,
                    availableFiles = uiState.availableFiles,
                    onAction = onAction,
                )

                PinButton(
                    isPinned = uiState.isPinned,
                    pinPosition = uiState.pinPosition,
                    onPinSwitch = {
                        onAction(ProjectRow.Actions.TogglePinnedClicked(uiState.projectId))
                    },
                )

                ProjectContextMenu(
                    onAction = { action ->
                        when (action) {
                            ProjectContextMenu.Actions.AddProjectClicked -> {
                                onAction(ProjectRow.Actions.AddProjectClicked(uiState.projectId))
                            }

                            ProjectContextMenu.Actions.DeleteClicked -> {
                                onAction(ProjectRow.Actions.DeleteClicked(uiState.projectId))
                            }
                        }
                    },
                )
                if (showPinnedReorderControls) {
                    OutlinedButton(
                        onClick = { onAction(ProjectRow.Actions.MovePinnedUpClicked(uiState.projectId)) },
                        enabled = uiState.canMovePinnedUp,
                    ) {
                        Text("Up")
                    }
                    OutlinedButton(
                        onClick = { onAction(ProjectRow.Actions.MovePinnedDownClicked(uiState.projectId)) },
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

@Composable
private fun OpenFileSplitButton(
    projectId: Long,
    initialSelectedFile: ProjectFile,
    availableFiles: List<FileName>,
    onAction: (ProjectRow.Actions) -> Unit,
) {
    var selectedFile by remember {
        mutableStateOf(initialSelectedFile)
    }

    val currentOptionIdx = availableFiles.indexOfFirst { it.fileId == selectedFile }

    OptionsSplitButton(
        options = availableFiles.map { it.fileName },
        currentOptionIdx = currentOptionIdx,
        onOptionClicked = { optionIdx ->
            if (optionIdx != null) {
                val clickedFile = availableFiles[optionIdx].fileId
                selectedFile = clickedFile
                onAction(
                    ProjectRow.Actions.OpenFileClicked(
                        projectId = projectId,
                        file = clickedFile,
                    )
                )
            }
        }
    )
}

object ProjectRowTestTags {
    fun row(projectId: Long): String = "project-row-$projectId"
    fun path(projectId: Long): String = "project-row-$projectId-path"
    fun buttonSetCurrent(projectId: Long): String = "project-row-$projectId-set-current"
}
