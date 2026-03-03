package gtr.mpfocus.ui.core

actual fun createAppWindowLauncher(): AppWindowLauncher = AndroidAppWindowLauncher

private object AndroidAppWindowLauncher : AppWindowLauncher {
    override suspend fun showWindow(window: AppWindowSpec) {
        TODO("Not yet implemented")
    }
}
