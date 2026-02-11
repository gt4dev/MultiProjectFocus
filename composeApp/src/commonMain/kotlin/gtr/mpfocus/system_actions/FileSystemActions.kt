package gtr.mpfocus.system_actions

import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.use

interface FileSystemActions {

    fun pathExists(path: FilePath): Boolean
    fun pathExists(path: FolderPath): Boolean

    fun createFolder(path: FolderPath): Boolean
    fun createFile(path: FilePath): Boolean
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

    override fun createFile(path: FilePath): Boolean {
        FileSystem.SYSTEM.sink(path.path).use { }
        return true
    }

    companion object {
        private fun pathExists(path: Path): Boolean {
            val exists = FileSystem.SYSTEM.exists(path)
            return exists
        }
    }
}
