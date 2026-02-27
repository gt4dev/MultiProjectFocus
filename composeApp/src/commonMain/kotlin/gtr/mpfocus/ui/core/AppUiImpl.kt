package gtr.mpfocus.ui.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class AppUiImpl : AppUi {
    private val appWindowLauncher: AppWindowLauncher = createAppWindowLauncher()

    override suspend fun showMessage(msg: Message) {
        when (msg) {
            is TextMessage -> {
                appWindowLauncher.showWindow({
                    MessageContent(messageText = msg.text)
                })
            }
        }
    }
}

@Composable
fun MessageContent(messageText: String) {
    MaterialTheme {
        Text(
            text = messageText,
            modifier = Modifier.padding(16.dp),
        )
    }
}

interface AppWindowLauncher {
    suspend fun showWindow(content: @Composable (() -> Unit))
}

expect fun createAppWindowLauncher(): AppWindowLauncher
