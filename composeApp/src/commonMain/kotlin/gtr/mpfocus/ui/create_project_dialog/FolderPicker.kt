package gtr.mpfocus.ui.create_project_dialog

interface FolderPicker {
    fun pickFolder(initialPath: String? = null): String?
}

expect fun createFolderPicker(): FolderPicker
