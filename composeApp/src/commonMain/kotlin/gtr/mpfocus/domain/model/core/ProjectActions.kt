package gtr.mpfocus.domain.model.core

sealed class ActionResult {
    object Success : ActionResult()
    data class Error(val msg: String) : ActionResult()
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
