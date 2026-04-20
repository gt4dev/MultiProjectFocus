package gtr.mpfocus.ui.create_file_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.ui.core.UiActions

object CreateFileDialog {

    data class StartParameters(
        val extraInfo: String?,
        val file: FilePath,
    )

    data class State(
        val topMessage: String? = null,
        val fileName: String = "",
        val folderPath: String = "",
        val errorMessage: String? = null,
        val isCreating: Boolean = false,
    )

    sealed interface Actions : UiActions {
        data object CreateFileClicked : Actions
        data object OpenFolderClicked : Actions
        data object CloseErrorClicked : Actions
        data object CancelClicked : Actions
    }
}

@Composable
fun CreateFileDialog(
    uiState: CreateFileDialog.State,
    onAction: (CreateFileDialog.Actions) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onAction(CreateFileDialog.Actions.CancelClicked)
        },
        title = {
            Text("Create file")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                uiState.topMessage?.let {
                    Text(it)
                }

                Text("File '${uiState.fileName}' doesn't exist. Create?")

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Folder:")
                    Text(uiState.folderPath)
                }

                uiState.errorMessage?.let { errorMessage ->
                    CreateFileDialogErrorPanel(
                        errorMessage = errorMessage,
                        onClose = { onAction(CreateFileDialog.Actions.CloseErrorClicked) },
                    )
                }
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextButton(
                    onClick = { onAction(CreateFileDialog.Actions.OpenFolderClicked) },
                ) {
                    Text("Open folder")
                }

                Button(
                    onClick = { onAction(CreateFileDialog.Actions.CreateFileClicked) },
                    enabled = !uiState.isCreating,
                ) {
                    Text("Create file")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onAction(CreateFileDialog.Actions.CancelClicked) },
            ) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun CreateFileDialogErrorPanel(
    errorMessage: String,
    onClose: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Couldn't create file:")
            Text(errorMessage)
            TextButton(
                onClick = onClose,
            ) {
                Text("Close")
            }
        }
    }
}
