package gtr.mpfocus.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.ui.core.UiActions

object CreateFolderPanel {

    data class State(
        val folderPath: FolderPath,
        val status: Status = Status.Ready,
    )

    sealed interface Status {
        data object Ready : Status
        data object InProgress : Status
        data object Success : Status
        data class Error(val reason: String) : Status
    }

    sealed interface Actions : UiActions {
        data object CreateClicked : Actions
        data object CloseClicked : Actions
    }
}

@Composable
fun CreateFolderPanel(
    state: CreateFolderPanel.State,
    onAction: (CreateFolderPanel.Actions) -> Unit,
) {
    val containerColor = when (state.status) {
        CreateFolderPanel.Status.Ready,
        CreateFolderPanel.Status.InProgress -> MaterialTheme.colorScheme.secondaryContainer

        CreateFolderPanel.Status.Success -> MaterialTheme.colorScheme.secondaryContainer
        is CreateFolderPanel.Status.Error -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (state.status) {
        CreateFolderPanel.Status.Ready,
        CreateFolderPanel.Status.InProgress -> MaterialTheme.colorScheme.onSecondaryContainer

        CreateFolderPanel.Status.Success -> MaterialTheme.colorScheme.onTertiaryContainer
        is CreateFolderPanel.Status.Error -> MaterialTheme.colorScheme.onErrorContainer
    }

    val title = when (state.status) {
        CreateFolderPanel.Status.Ready,
        CreateFolderPanel.Status.InProgress -> "Folder doesn't exist:"

        CreateFolderPanel.Status.Success -> "Folder created:"
        is CreateFolderPanel.Status.Error -> "Failed folder creation:"
    }
    val details = when (val status = state.status) {
        CreateFolderPanel.Status.Ready,
        CreateFolderPanel.Status.InProgress,
        CreateFolderPanel.Status.Success -> state.folderPath.path.toString()

        is CreateFolderPanel.Status.Error -> status.reason
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            when (state.status) {
                CreateFolderPanel.Status.Ready -> {
                    Button(
                        onClick = { onAction(CreateFolderPanel.Actions.CreateClicked) },
                    ) {
                        Text("Create\nfolder", textAlign = TextAlign.Center)
                    }
                }

                CreateFolderPanel.Status.InProgress -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                    )
                }

                CreateFolderPanel.Status.Success,
                is CreateFolderPanel.Status.Error -> {
                    TextButton(
                        onClick = { onAction(CreateFolderPanel.Actions.CloseClicked) },
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
