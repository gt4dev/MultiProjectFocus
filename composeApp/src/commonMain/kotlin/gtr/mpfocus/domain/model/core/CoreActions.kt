package gtr.mpfocus.domain.model.core

interface CoreActions {

    // current project actions

    suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences = ActionPreferences(),
        userNotifier: UserNotifier = UserNotifier.None
    ): ActionResult

    suspend fun openCurrentProjectFile(
        file: ProjectFiles,
        actionPreferences: ActionPreferences = ActionPreferences(),
        userNotifier: UserNotifier = UserNotifier.None
    ): ActionResult


    // pinned project actions

    suspend fun openPinnedProjectFolder(pinPosition: Int)

    suspend fun openPinnedProjectFile(pinPosition: Int, file: ProjectFiles)
}

interface UserNotifier {
    // notify user what to do cause there is eg: no folder, no file, no current project ..
    // user takes their action or cancels
    // then logic returns to work and check by itself current model state
    // eg: if folder/file/.. was in the meantime created or not

    suspend fun createFolder(folderName: String)
    suspend fun createFile(folderName: String)
    suspend fun setCurrentProject()

    object None : UserNotifier {
        override suspend fun createFolder(folderName: String) {}
        override suspend fun createFile(folderName: String) {}
        override suspend fun setCurrentProject() {}
    }
}


sealed class ActionResult {
    object Success : ActionResult()
    data class Error(val msg: String) : ActionResult()
}


data class ActionPreferences(
    val ifNoFileOrFolder: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
) {
    enum class IfNoFileOrFolder { AutoCreate, ReportError, NotifyUser }
}
