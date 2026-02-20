package gtr.mpfocus.system_actions

import okio.Path

data class FilePath(val path: Path)

data class FolderPath(val path: Path)


interface OperatingSystemActions {
    suspend fun openFile(f: FilePath)
    suspend fun openFolder(f: FolderPath)
}

expect suspend fun realOpenFile(f: FilePath)

expect suspend fun realOpenFolder(f: FolderPath)
