package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FilePath

sealed class ActionResult {
    object Success : ActionResult()
    object NoFileError : ActionResult()
    data class Error(val msg: String) : ActionResult()
}

interface ProjectActions {

    suspend fun openCurrentProjectFolder(
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    suspend fun openCurrentProjectFile(
        fileId: ProjectFile,
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
        fileId: ProjectFile,
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
        fileId: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult

    interface CallerNotification {
        suspend fun noFolder(): CallerDecision
        suspend fun noFile(filePath: FilePath): CallerDecision
        suspend fun noCurrentProject(): CallerDecision
        suspend fun noPinnedProject(): CallerDecision

        object CancelAll : CallerNotification {
            override suspend fun noFolder() = CallerDecision.Cancel

            override suspend fun noFile(filePath: FilePath) = CallerDecision.Cancel

            override suspend fun noCurrentProject() = CallerDecision.Cancel

            override suspend fun noPinnedProject() = CallerDecision.Cancel
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
