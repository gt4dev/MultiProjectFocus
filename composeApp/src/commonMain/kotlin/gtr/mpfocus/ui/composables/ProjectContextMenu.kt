package gtr.mpfocus.ui.composables


import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import gtr.mpfocus.ui.core.UiActions

object ProjectContextMenu {

    sealed interface Actions : UiActions {
        data object DeleteClicked : Actions

        data object AddProjectClicked : Actions
    }
}

@Composable
fun ProjectContextMenu(
    onAction: (ProjectContextMenu.Actions) -> Unit,
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
                text = { Text("Add project ...") },
                onClick = {
                    expanded = false
                    onAction(ProjectContextMenu.Actions.AddProjectClicked)
                },
            )
            DropdownMenuItem(
                text = { Text("Delete ...") },
                onClick = {
                    expanded = false
                    onAction(ProjectContextMenu.Actions.DeleteClicked)
                },
            )
        }
    }
}
