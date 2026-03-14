package gtr.mpfocus.domain.model.core

interface CoreActions {

    // current project actions

    suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult

    suspend fun openCurrentProjectFile(
        file: ProjectFile,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult


    // pinned project actions

    suspend fun openPinnedProjectFolder(
        pinPosition: Int,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult

    suspend fun openPinnedProjectFile(
        pinPosition: Int,
        file: ProjectFile,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult
}

interface UserNotifier {
    // notify user what to do cause there is eg: no folder, no file, no current project ..
    // user takes their action or cancels
    // then logic returns to work and check by itself current model state
    // eg: if folder/file/.. was in the meantime created or not

    suspend fun createFolder(folderName: String)
    suspend fun createFile(folderName: String)
    suspend fun setCurrentProject()
    suspend fun setPinnedProject(pinPosition: Int)

    object None : UserNotifier {
        override suspend fun createFolder(folderName: String) {}
        override suspend fun createFile(folderName: String) {}
        override suspend fun setCurrentProject() {}
        override suspend fun setPinnedProject(pinPosition: Int) {}
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


interface ProjectActions {

    suspend fun openCurrentProjectFolder(
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    suspend fun openCurrentProjectFile(
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    suspend fun openPinnedProjectFolder(
        pinPosition: Int,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    suspend fun openPinnedProjectFile(
        pinPosition: Int,
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    suspend fun openRegularProjectFolder(
        projectId: Long,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    suspend fun openRegularProjectFile(
        projectId: Long,
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    interface CallerNotification {
        suspend fun noFolder(folderName: String): CallerDecision
        suspend fun noFile(fileName: String): CallerDecision
        suspend fun noCurrentProject(): CallerDecision
        suspend fun noPinnedProject(pinPosition: Int): CallerDecision

        object CancelAll : CallerNotification {
            override suspend fun noFolder(folderName: String) = CallerDecision.Cancel

            override suspend fun noFile(fileName: String) = CallerDecision.Cancel

            override suspend fun noCurrentProject() = CallerDecision.Cancel

            override suspend fun noPinnedProject(pinPosition: Int) = CallerDecision.Cancel
        }

        enum class CallerDecision { Continue, Cancel }
    }

    data class Preferences(
        val ifNoFileOrFolder: PrefValue,
        val ifNoCurrentProject: PrefValue,
        val ifNoPinnedProject: PrefValue,
    ) {
        enum class PrefValue { NotifyCaller, ReturnError }

        companion object {
            val UI = Preferences(
                PrefValue.NotifyCaller,
                PrefValue.ReturnError,
                PrefValue.ReturnError
            )
            val CLI = Preferences(
                PrefValue.ReturnError,
                PrefValue.ReturnError,
                PrefValue.ReturnError
            )
        }
    }
}