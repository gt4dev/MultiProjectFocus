package gtr.mpfocus.system_actions


interface OperatingSystemActions {
    suspend fun openFile(f: FilePath)
    suspend fun openFolder(f: FolderPath)
}

expect suspend fun realOpenFile(f: FilePath)

expect suspend fun realOpenFolder(f: FolderPath)
