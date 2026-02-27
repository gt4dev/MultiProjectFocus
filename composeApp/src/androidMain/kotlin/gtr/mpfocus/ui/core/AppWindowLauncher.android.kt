package gtr.mpfocus.ui.core

import androidx.compose.runtime.Composable

actual fun createAppWindowLauncher(): AppWindowLauncher = AndroidAppWindowLauncher

private object AndroidAppWindowLauncher : AppWindowLauncher {
    override suspend fun showWindow(content: @Composable (() -> Unit)) {
        TODO("Not yet implemented")
    }
}
