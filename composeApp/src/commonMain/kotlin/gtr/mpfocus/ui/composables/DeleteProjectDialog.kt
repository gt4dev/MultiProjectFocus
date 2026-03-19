package gtr.mpfocus.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import gtr.mpfocus.ui.core.UiActions

object DeleteProjectDialog {

    data class State(
        val path: String
    )

    sealed interface Actions : UiActions {
        data object ConfirmClicked : Actions
        data object CancelClicked : Actions
    }

}

@Composable
fun DeleteProjectDialog(
    state: DeleteProjectDialog.State,
    onAction: (DeleteProjectDialog.Actions) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onAction(DeleteProjectDialog.Actions.CancelClicked)
        },
        title = {
            Text("Delete project")
        },
        text = {
            Text(
                """
                Do you want to delete this project?
                
                ${state.path}
                     
                "Delete" does NOT DELETE this folder.
                "Delete" removes this project from MPF.
                """.trimIndent()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onAction(DeleteProjectDialog.Actions.ConfirmClicked)
                },
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(DeleteProjectDialog.Actions.CancelClicked)
                },
            ) {
                Text("Cancel")
            }
        },
    )
}
