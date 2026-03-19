package gtr.mpfocus.ui.main_screen

sealed interface MainScreenEffect {
    data class CreateProjectDialogRequested(val relatedProjectId: Long?) : MainScreenEffect
}
