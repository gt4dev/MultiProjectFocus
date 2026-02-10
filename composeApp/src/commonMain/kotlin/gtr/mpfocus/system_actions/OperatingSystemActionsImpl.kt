package gtr.mpfocus.system_actions

class OperatingSystemActionsImpl : OperatingSystemActions {
    override suspend fun openFile(f: FilePath) {
        realOpenFile(f)
    }

    override suspend fun openFolder(f: FolderPath) {
        realOpenFolder(f)
    }
}