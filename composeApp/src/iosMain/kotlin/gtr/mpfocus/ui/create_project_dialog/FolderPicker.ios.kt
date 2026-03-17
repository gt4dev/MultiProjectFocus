package gtr.mpfocus.ui.create_project_dialog

actual fun createFolderPicker(): FolderPicker = IosFolderPicker

private object IosFolderPicker : FolderPicker {
    override fun pickFolder(initialPath: String?): String? = null
}
