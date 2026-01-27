package gtr.mpfocus.domain.model.platform

class OperatingSystemActions {
    suspend fun openFile(f: MPFile) {
        realOpenFile(f)
    }
    suspend fun openFolder(f: MPFolder) {
        realOpenFolder(f)
    }
}

expect suspend fun realOpenFile(f: MPFile)

expect suspend fun realOpenFolder(f: MPFolder)

expect class MPFile(initPath: String) {
    val path: String
}

expect class MPFolder(initPath: String) {
    val path: String
}