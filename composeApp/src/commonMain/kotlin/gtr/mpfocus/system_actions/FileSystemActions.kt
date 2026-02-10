package gtr.mpfocus.system_actions

import okio.FileSystem
import okio.Path
import okio.SYSTEM

interface FileSystemActions {

    fun pathExists(path: FilePath): Boolean
    fun pathExists(path: FolderPath): Boolean

    fun createFolder(path: FolderPath): Boolean
}

class FileSystemActionsImpl : FileSystemActions {

    override fun pathExists(path: FilePath): Boolean {
        return pathExists(path.path)
    }

    override fun pathExists(path: FolderPath): Boolean {
        return pathExists(path.path)
    }

    override fun createFolder(path: FolderPath): Boolean {
        FileSystem.SYSTEM.createDirectories(path.path)
        return true
    }

    companion object {
        private fun pathExists(path: Path): Boolean {
            val exists = FileSystem.SYSTEM.exists(path)
            return exists
        }
    }
}