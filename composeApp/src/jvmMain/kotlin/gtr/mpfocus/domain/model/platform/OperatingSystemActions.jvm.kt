package gtr.mpfocus.domain.model.platform

import java.io.File

actual class MPFile actual constructor(initPath: String) {
    actual val path = initPath
}

actual class MPFolder actual constructor(initPath: String) {
    actual val path = initPath
}

actual suspend fun realOpenFile(f: MPFile) {
    val file = File(f.path)
    require(file.exists()) { "File does not exist: ${file.absolutePath}" }
    require(file.isFile()) { "It's not file: ${file.absolutePath}" }
    val os = System.getProperty("os.name").lowercase()
    val cmd = when {
        os.contains("win") -> listOf("cmd", "/c", "start", "", file.absolutePath)
        os.contains("mac") -> listOf("open", file.absolutePath)
        else -> listOf("xdg-open", file.absolutePath)
    }
    ProcessBuilder(cmd).start() // todo: fix warning
}


actual suspend fun realOpenFolder(f: MPFolder) {
    val folder = File(f.path)
    require(folder.exists() && folder.isDirectory)
    val os = System.getProperty("os.name").lowercase()
    val path = folder.absolutePath
    val command = when {
        os.contains("win") -> listOf("explorer", path)
        os.contains("mac") -> listOf("open", path)
        else -> listOf("xdg-open", path)
    }

    ProcessBuilder(command)
        .redirectErrorStream(true)
        .start() // todo: fix warning
}