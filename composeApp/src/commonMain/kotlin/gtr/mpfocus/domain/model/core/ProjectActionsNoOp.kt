package gtr.mpfocus.domain.model.core

// todo-soon: remove
object ProjectActionsNoOp : ProjectActions {

    private const val NOT_IMPLEMENTED_ERROR = "ProjectActions is not implemented yet."

    override suspend fun openCurrentProjectFolder(
        actionPreferences: ProjectActions.Preferences,
        callback: ProjectActions.Callback
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openCurrentProjectFile(
        file: ProjectFile,
        actionPreferences: ProjectActions.Preferences,
        callback: ProjectActions.Callback
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openPinnedProjectFolder(
        pinPosition: Int,
        actionPreferences: ProjectActions.Preferences,
        callback: ProjectActions.Callback
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openPinnedProjectFile(
        pinPosition: Int,
        file: ProjectFile,
        actionPreferences: ProjectActions.Preferences,
        callback: ProjectActions.Callback
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openRegularProjectFolder(
        projectId: Long,
        actionPreferences: ProjectActions.Preferences,
        callback: ProjectActions.Callback
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openRegularProjectFile(
        projectId: Long,
        file: ProjectFile,
        actionPreferences: ProjectActions.Preferences,
        callback: ProjectActions.Callback
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)
}
