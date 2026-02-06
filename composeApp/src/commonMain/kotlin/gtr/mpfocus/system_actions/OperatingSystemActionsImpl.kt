package gtr.mpfocus.system_actions

class OperatingSystemActionsImpl : OperatingSystemActions {
    override suspend fun openFile(f: MPFile) {
        realOpenFile(f)
    }

    override suspend fun openFolder(f: MPFolder) {
        realOpenFolder(f)
    }
}