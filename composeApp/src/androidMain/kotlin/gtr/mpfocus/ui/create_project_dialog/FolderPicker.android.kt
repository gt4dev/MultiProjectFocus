package gtr.mpfocus.ui.create_project_dialog

actual fun createFolderPicker(): FolderPicker = AndroidFolderPicker

private object AndroidFolderPicker : FolderPicker {
    override fun pickFolder(initialPath: String?): String? = null
}
