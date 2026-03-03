package gtr.mpfocus.ui.core

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

actual fun createAppWindowLauncher(): AppWindowLauncher = JvmAppWindowLauncher

private object JvmAppWindowLauncher : AppWindowLauncher {
    override suspend fun showWindow(window: AppWindowSpec) {
        singleWindowApplication(
            title = window.title,
            state = WindowState(
                size = DpSize(800.dp, 1000.dp)
            )
        ) {
            window.content()
        }
    }
}
