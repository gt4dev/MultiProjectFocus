package gtr.mpfocus.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.singleWindowApplication

actual fun createAppWindowLauncher(): AppWindowLauncher = JvmAppWindowLauncher

private object JvmAppWindowLauncher : AppWindowLauncher {
    override suspend fun showWindow(content: @Composable () -> Unit) {
        singleWindowApplication(
            title = "Multi Project Focus",
        ) {
            content()
        }
    }
}
