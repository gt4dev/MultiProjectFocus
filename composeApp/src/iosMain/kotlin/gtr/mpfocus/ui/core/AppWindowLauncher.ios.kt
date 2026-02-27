package gtr.mpfocus.ui.core

import androidx.compose.runtime.Composable

actual fun createAppWindowLauncher(): AppWindowLauncher = IosAppWindowLauncher

private object IosAppWindowLauncher : AppWindowLauncher {
    override suspend fun showWindow(content: @Composable (() -> Unit)) {
        TODO("Not yet implemented")
    }
}
