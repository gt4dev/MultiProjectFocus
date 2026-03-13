package gtr.mpfocus.ui.composables


import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import gtr.mpfocus.ui.core.UiActions

data class ProjectContextMenuState(
    val projectId: Long,
)

sealed interface ProjectContextMenuUiActions : UiActions {
    data object DeleteClicked : ProjectContextMenuUiActions
    data object AddSubProjectClicked : ProjectContextMenuUiActions
}

@Composable
fun ProjectContextMenu(
    uiState: ProjectContextMenuState,
    onAction: (ProjectContextMenuUiActions) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilledTonalIconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More actions",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onAction(ProjectContextMenuUiActions.DeleteClicked)
                },
            )
            DropdownMenuItem(
                text = { Text("Add sub-project") },
                onClick = {
                    expanded = false
                    onAction(ProjectContextMenuUiActions.AddSubProjectClicked)
                },
            )
        }
    }
}
