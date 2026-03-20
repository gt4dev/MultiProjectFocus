package gtr.mpfocus

/**
 * On some Windows machines [with old GPU/Drivers] can occur problem with running Compose on DirectX.
 * Log is generated:
 *   [SKIKO] warn: Fallback to next API
 *   org.jetbrains.skiko.RenderException: Failed to create DirectX12 device.
 * And global system hang can occur.
 *
 * To avoid it, Windows always use OPENGL, instead of DirectX.
 * UI performance for MPF is "the same", but stability higher.
 */
fun fixSkiko() {
    val osName = System.getProperty("os.name") ?: ""
    if (osName.startsWith("Windows", ignoreCase = true)) {
        System.setProperty("skiko.renderApi", "OPENGL")
    }
}