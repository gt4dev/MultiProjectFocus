package gtr.mpfocus.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gtr.mpfocus.ui.composables.MessagePanelState.Tone
import gtr.mpfocus.ui.core.UiActions

data class MessagePanelState(
    val text: String,
    val tone: Tone = Tone.Info,
) {
    enum class Tone {
        Info,
        Warning,
        Error,
    }
}

sealed interface MessagePanelUiActions : UiActions {
    data object DismissClicked : MessagePanelUiActions
}

@Composable
fun MessagePanel(
    uiState: MessagePanelState,
    onAction: (MessagePanelUiActions) -> Unit,
) {
    val containerColor = when (uiState.tone) {
        Tone.Info -> MaterialTheme.colorScheme.secondaryContainer
        Tone.Warning -> MaterialTheme.colorScheme.tertiaryContainer
        Tone.Error -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (uiState.tone) {
        Tone.Info -> MaterialTheme.colorScheme.onSecondaryContainer
        Tone.Warning -> MaterialTheme.colorScheme.onTertiaryContainer
        Tone.Error -> MaterialTheme.colorScheme.onErrorContainer
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = uiState.text,
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(
                onClick = { onAction(MessagePanelUiActions.DismissClicked) },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Dismiss")
            }
        }
    }
}
