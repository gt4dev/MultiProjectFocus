package gtr.mpfocus.ui.core

import androidx.compose.runtime.Composable
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.main_screen.MainScreenWindowFactory

class AppUiImpl(
    private val appWindowLauncher: AppWindowLauncher,
    private val mainScreenWindowFactory: MainScreenWindowFactory,
) : AppUi {

    override suspend fun showMessage(msg: Message) {
        when (msg) {
            is TextMessage -> {
                appWindowLauncher.showWindow(
                    mainScreenWindowFactory.create(
                        initialMessage = MessagePanelState(text = msg.text)
                    )
                )
            }
        }
    }
}

data class AppWindowSpec(
    val title: String,
    val content: @Composable () -> Unit,
)

interface AppWindowLauncher {
    suspend fun showWindow(window: AppWindowSpec)
}

expect fun createAppWindowLauncher(): AppWindowLauncher
