package gtr.mpfocus.system_actions

import okio.Path

class FilePath(val path: Path)

class FolderPath(val path: Path)


interface OperatingSystemActions {
    suspend fun openFile(f: FilePath)
    suspend fun openFolder(f: FolderPath)
}

expect suspend fun realOpenFile(f: FilePath)

expect suspend fun realOpenFolder(f: FolderPath)
