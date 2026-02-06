package gtr.mpfocus.system_actions

expect class MPFile(initPath: String) {
    val path: String
}

expect class MPFolder(initPath: String) {
    val path: String
}

interface OperatingSystemActions {
    suspend fun openFile(f: MPFile)
    suspend fun openFolder(f: MPFolder)
}

expect suspend fun realOpenFile(f: MPFile)

expect suspend fun realOpenFolder(f: MPFolder)