package gtr.mpfocus.domain.model.core

interface CoreActions {

    // current project actions

    suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences = ActionPreferences(),
        userInstructor: UserInstructor = UserInstructor.None
    ): ActionResult

    suspend fun openCurrentProjectFile(file: ProjectKnownFiles)


    // pinned project actions

    suspend fun openPinnedProjectFolder(pinPosition: Int)

    suspend fun openPinnedProjectFile(pinPosition: Int, file: ProjectKnownFiles)
}

interface UserInstructor {
    // send to user info what to do if there is eg: no folder, no file, no current project ..
    // user takes their action or cancels
    // then logic returns to work and check by itself current model state
    // eg: if folder/file/.. was in the meantime created or not

    suspend fun createFolder(folderName: String)
    suspend fun createFile(folderName: String)
    suspend fun setCurrentProject()

    object None : UserInstructor {
        override suspend fun createFolder(folderName: String) {}
        override suspend fun createFile(folderName: String) {}
        override suspend fun setCurrentProject() {}
    }
}


// todo: replace with TypedResult<Nothing>
sealed class ActionResult {
    object Success : ActionResult()
    data class Error(val msg: String) : ActionResult()
}


data class ActionPreferences(
    // todo: join to 1 pref `ifNoFileOrFolder`
    val ifNoFolder: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
    val ifNoFile: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
) {
    enum class IfNoFileOrFolder { AutoCreate, ReportError, InstructUser }
}
