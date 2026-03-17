package gtr.mpfocus.ui.create_project_dialog

import java.io.File
import javax.swing.JFileChooser

actual fun createFolderPicker(): FolderPicker = JvmFolderPicker

private object JvmFolderPicker : FolderPicker {
    override fun pickFolder(initialPath: String?): String? {
        val chooser = JFileChooser(initialPath?.takeIf { it.isNotBlank() }?.let(::File)).apply {
            dialogTitle = "Select project folder"
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isAcceptAllFileFilterUsed = false
        }

        val result = chooser.showOpenDialog(null)
        return if (result == JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile?.absolutePath
        } else {
            null
        }
    }
}
