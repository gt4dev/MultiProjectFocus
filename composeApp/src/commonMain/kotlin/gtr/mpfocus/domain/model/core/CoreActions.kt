package gtr.mpfocus.domain.model.core

interface CoreActions {

    // current project actions

    suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences = ActionPreferences()
    )

    suspend fun openCurrentProjectFile(file: ProjectKnownFiles)


    // pinned project actions

    suspend fun openPinnedProjectFolder(pinPosition: Int)

    suspend fun openPinnedProjectFile(pinPosition: Int, file: ProjectKnownFiles)
}


data class ActionPreferences(
    val ifNoFolder: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
    val ifNoFile: IfNoFileOrFolder = IfNoFileOrFolder.ReportError,
) {
    enum class IfNoFileOrFolder { AutoCreate, ReportError, AskUser }
}
