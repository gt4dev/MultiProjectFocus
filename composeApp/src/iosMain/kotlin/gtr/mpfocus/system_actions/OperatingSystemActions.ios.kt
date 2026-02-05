package gtr.mpfocus.system_actions

actual class MPFile actual constructor(initPath: String) {
    actual val path: String
        get() = TODO()
}

actual class MPFolder actual constructor(initPath: String) {
    actual val path: String
        get() = TODO()
}

actual suspend fun realOpenFile(f: MPFile) {
    TODO()
}


actual suspend fun realOpenFolder(f: MPFolder) {
    TODO()
}