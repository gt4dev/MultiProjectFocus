package gtr.mpfocus.domain.model.core

interface CoreActions {

    // current project actions

    suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences = ActionPreferences(),
        userInstructor: UserInstructor
    ): ActionResult

    suspend fun openCurrentProjectFile(file: ProjectKnownFiles)


    // pinned project actions

    suspend fun openPinnedProjectFolder(pinPosition: Int)

    suspend fun openPinnedProjectFile(pinPosition: Int, file: ProjectKnownFiles)
}

interface UserInstructor {
    // send to user info what to do if there is a lack, eg: no folder, no file, no current project ..
    // don't check user actions, just wait for 'end of handling'
    // user takes their action or cancels
    // then logic returns to work and check by itself current state
    // eg: if folder/file/.. was in the meantime created or not

    suspend fun createFolder(folderName: String)
    suspend fun createFile(folderName: String)
    suspend fun setCurrentProject()
}


sealed class ActionResult {
    object Success : ActionResult()
    data class Error(val msg: String) : ActionResult()
    object Cancel : ActionResult()
}


data class ActionPreferences(
    val ifNoFolder: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
    val ifNoFile: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
) {
    enum class IfNoFileOrFolder { AutoCreate, ReportError, InstructUser }
}
