package gtr.mpfocus.domain.model.core

interface CoreActions {

    // current project actions

    suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences = ActionPreferences()
    ): ActionResult

    suspend fun openCurrentProjectFile(file: ProjectKnownFiles)


    // pinned project actions

    suspend fun openPinnedProjectFolder(pinPosition: Int)

    suspend fun openPinnedProjectFile(pinPosition: Int, file: ProjectKnownFiles)
}


sealed class ActionResult {
    object Success : ActionResult()
    object Failure : ActionResult()
    object Cancel : ActionResult()
}


data class ActionPreferences(
    val ifNoFolder: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
    val ifNoFile: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
) {
    enum class IfNoFileOrFolder { AutoCreate, ReportError, AskUser }
}
