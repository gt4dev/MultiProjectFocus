package gtr.mpfocus.ui.composables


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.ui.core.UiActions

data class ProjectFileSelectorState(
    val selectedFile: ProjectFile = ProjectFile.File1,
    val availableFiles: List<ProjectFile> = ProjectFile.entries,
)

sealed interface ProjectFileSelectorUiActions : UiActions {
    data class FileSelected(val file: ProjectFile) : ProjectFileSelectorUiActions
}

@Composable
fun ProjectFileSelector(
    uiState: ProjectFileSelectorState,
    onAction: (ProjectFileSelectorUiActions) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.widthIn(min = 128.dp),
        ) {
            Text(uiState.selectedFile.name)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            uiState.availableFiles.forEach { file ->
                DropdownMenuItem(
                    text = { Text(file.name) },
                    onClick = {
                        expanded = false
                        onAction(ProjectFileSelectorUiActions.FileSelected(file))
                    },
                )
            }
        }
    }
}
