package gtr.mpfocus.ui.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.core.UiActions

data class ScreenHeaderState(
    val title: String = "Multi Project Focus",
    val subtitle: String = "Current, pinned, and other projects in one screen.",
    val addProjectButtonLabel: String = "Add project",
)

sealed interface ScreenHeaderUiActions : UiActions {
    data object AddProjectClicked : ScreenHeaderUiActions

    data object ProjectConfigClicked : ScreenHeaderUiActions
}

@Composable
fun ScreenHeader(
    uiState: ScreenHeaderState,
    onAction: (ScreenHeaderUiActions) -> Unit,
) {
    var settingsMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = uiState.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = uiState.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                IconButton(onClick = { settingsMenuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                    )
                }
                DropdownMenu(
                    expanded = settingsMenuExpanded,
                    onDismissRequest = { settingsMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Global project config") },
                        onClick = {
                            settingsMenuExpanded = false
                            onAction(ScreenHeaderUiActions.ProjectConfigClicked)
                        },
                    )
                }
            }
            Button(onClick = { onAction(ScreenHeaderUiActions.AddProjectClicked) }) {
                Text(uiState.addProjectButtonLabel)
            }
        }
    }
}
