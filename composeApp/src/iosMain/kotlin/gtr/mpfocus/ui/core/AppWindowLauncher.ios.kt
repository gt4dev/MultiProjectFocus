package gtr.mpfocus.ui.core

actual fun createAppWindowLauncher(): AppWindowLauncher = IosAppWindowLauncher

private object IosAppWindowLauncher : AppWindowLauncher {
    override suspend fun showWindow(window: AppWindowSpec) {
        TODO("Not yet implemented")
    }
}
