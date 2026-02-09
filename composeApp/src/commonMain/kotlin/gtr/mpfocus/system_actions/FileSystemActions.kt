package gtr.mpfocus.system_actions

import okio.FileSystem
import okio.Path
import okio.SYSTEM

interface FileSystemActions {

    // check whether file OR folder exists
    fun pathExists(path: Path): Boolean

    fun createFolder(path: Path): Boolean
}

class FileSystemActionsImpl : FileSystemActions {

    override fun pathExists(path: Path): Boolean {
        val exists = FileSystem.SYSTEM.exists(path)
        return exists
    }

    override fun createFolder(path: Path): Boolean {
        FileSystem.SYSTEM.createDirectories(path)
        return true
    }
}