package gtr.mpfocus.ui.main_screen

import gtr.mpfocus.ui.create_file_dialog.CreateFileDialog

sealed interface MainScreenEffect {
    data class CreateProjectDialogRequested(val relatedProjectId: Long?) : MainScreenEffect
    data class CreateFileDialogRequested(
        val startParameters: CreateFileDialog.StartParameters,
    ) : MainScreenEffect
}
