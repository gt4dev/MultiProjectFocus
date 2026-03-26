package gtr.mpfocus.ui.create_project_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.composables.CreateFolderPanel
import gtr.mpfocus.ui.core.UiActions


object CreateProjectDialog {

    object TestTags {
        const val PROJECT_PATH_INPUT = "create-project-dialog-project-path-input"
        const val SET_AS_CURRENT_CHECKBOX = "create-project-dialog-set-as-current-checkbox"
        const val ADD_BUTTON = "create-project-dialog-add-button"
    }

    data class State(
        val projectPath: String = "",
        val setAsCurrent: Boolean = false,
        val projectPathError: String? = null,
        val isSubmitting: Boolean = false,
        val createFolderPanel: CreateFolderPanel.State? = null,
    )

    sealed interface Actions : UiActions {
        data class ProjectPathChanged(val value: String) : Actions
        data class SetAsCurrentChanged(val value: Boolean) : Actions
        data object BrowseClicked : Actions
        data object CreateClicked : Actions
        data object CloseClicked : Actions
        data class CreateFolderPanelAction(val action: CreateFolderPanel.Actions) : Actions
    }
}


@Composable
fun CreateProjectDialog(
    uiState: CreateProjectDialog.State,
    onAction: (CreateProjectDialog.Actions) -> Unit,
) {
    val isBusy = uiState.isSubmitting || uiState.createFolderPanel?.status is CreateFolderPanel.Status.InProgress

    AlertDialog(
        onDismissRequest = {
            if (!isBusy) {
                onAction(CreateProjectDialog.Actions.CloseClicked)
            }
        },
        title = {
            Text("Add project")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = uiState.projectPath,
                    onValueChange = { onAction(CreateProjectDialog.Actions.ProjectPathChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(CreateProjectDialog.TestTags.PROJECT_PATH_INPUT),
                    singleLine = true,
                    label = { Text("Project folder path") },
                    trailingIcon = {
                        IconButton(
                            onClick = { onAction(CreateProjectDialog.Actions.BrowseClicked) },
                            enabled = !isBusy,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Folder,
                                contentDescription = "Browse for project folder",
                            )
                        }
                    },
                    supportingText = {
                        uiState.projectPathError?.let {
                            Text(it)
                        }
                    },
                    isError = uiState.projectPathError != null,
                    enabled = !isBusy,
                )

                uiState.createFolderPanel?.let { createFolderPanelState ->
                    CreateFolderPanel(
                        state = createFolderPanelState,
                        onAction = { action ->
                            onAction(CreateProjectDialog.Actions.CreateFolderPanelAction(action))
                        },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        modifier = Modifier.testTag(CreateProjectDialog.TestTags.SET_AS_CURRENT_CHECKBOX),
                        checked = uiState.setAsCurrent,
                        onCheckedChange = { onAction(CreateProjectDialog.Actions.SetAsCurrentChanged(it)) },
                        enabled = !isBusy,
                    )
                    Text(
                        text = "Set as current project",
                        modifier = Modifier.clickable(enabled = !isBusy) {
                            onAction(
                                CreateProjectDialog.Actions.SetAsCurrentChanged(!uiState.setAsCurrent)
                            )
                        },
                    )
                }
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.testTag(CreateProjectDialog.TestTags.ADD_BUTTON),
                onClick = { onAction(CreateProjectDialog.Actions.CreateClicked) },
                enabled = !isBusy,
            ) {
                Text(if (uiState.isSubmitting) "Adding..." else "Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onAction(CreateProjectDialog.Actions.CloseClicked) },
                enabled = !isBusy,
            ) {
                Text("Cancel")
            }
        },
    )
}
