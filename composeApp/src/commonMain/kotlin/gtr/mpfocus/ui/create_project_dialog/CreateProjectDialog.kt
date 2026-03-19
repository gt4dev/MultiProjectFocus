package gtr.mpfocus.ui.create_project_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.core.UiActions


object CreateProjectDialog {

    data class State(
        val projectPath: String = "",
        val projectPathError: String? = null,
        val isSubmitting: Boolean = false,
    )

    sealed interface Actions : UiActions {
        data class ProjectPathChanged(val value: String) : Actions
        data object BrowseClicked : Actions
        data object CreateClicked : Actions
        data object CloseClicked : Actions
    }
}


@Composable
fun CreateProjectDialog(
    uiState: CreateProjectDialog.State,
    onAction: (CreateProjectDialog.Actions) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            if (!uiState.isSubmitting) {
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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Project folder path") },
                    trailingIcon = {
                        IconButton(
                            onClick = { onAction(CreateProjectDialog.Actions.BrowseClicked) },
                            enabled = !uiState.isSubmitting,
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
                    enabled = !uiState.isSubmitting,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAction(CreateProjectDialog.Actions.CreateClicked) },
                enabled = !uiState.isSubmitting,
            ) {
                Text(if (uiState.isSubmitting) "Adding..." else "Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onAction(CreateProjectDialog.Actions.CloseClicked) },
                enabled = !uiState.isSubmitting,
            ) {
                Text("Cancel")
            }
        },
    )
}
