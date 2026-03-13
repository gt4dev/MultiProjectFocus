package gtr.mpfocus.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    headerActions: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                if (headerActions != null) {
                    headerActions()
                }
            }
            HorizontalDivider()
            content()
        }
    }
}

@Composable
internal fun EmptySectionText(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
internal fun PinButton(
    isPinned: Boolean,
    onPinSwitch: () -> Unit,
) {
    FilledTonalIconButton(
        onClick = onPinSwitch,
    ) {
        if (isPinned) {
            Icon(
                imageVector = Icons.Filled.PushPin,
                contentDescription = "Unpin project",
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.PushPin,
                contentDescription = "Pin project",
            )
        }
    }
}
