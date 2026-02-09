package gtr.mpfocus.system_actions

// todo: remove MPFile i MPFolder, use directly okio.Path
// todo: also use Path in userInstructor.createFolder etc...
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