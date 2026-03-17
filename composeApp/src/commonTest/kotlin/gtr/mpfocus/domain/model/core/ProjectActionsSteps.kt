package gtr.mpfocus.domain.model.core

import dev.hotest.HOTestCtx
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend

object ProjectActionsSteps {

    fun HOTestCtx.`given 'project actions mock' exists`() {
        val obj = mock<ProjectActions>(MockMode.autofill)

        everySuspend { obj.openCurrentProjectFolder(any(), any()) } returns ActionResult.Success
        everySuspend { obj.openCurrentProjectFile(any(), any(), any()) } returns ActionResult.Success
        everySuspend { obj.openPinnedProjectFolder(any(), any(), any()) } returns ActionResult.Success
        everySuspend { obj.openPinnedProjectFile(any(), any(), any(), any()) } returns ActionResult.Success
        everySuspend { obj.openRegularProjectFolder(any(), any(), any()) } returns ActionResult.Success
        everySuspend { obj.openRegularProjectFile(any(), any(), any(), any()) } returns ActionResult.Success

        koinAdd {
            single<ProjectActions> {
                obj
            }
        }
    }

    fun HOTestCtx.`given 'real project actions' exists`() {
        koinAdd {
            single {
                ProjectActionsImpl(
                    get(),
                    get(),
                    get(),
                    get()
                )
            }
            single<ProjectActions> { get<ProjectActionsImpl>() }
        }
    }

    fun HOTestCtx.`given 'project action preferences' is set`(
        ifNoFileOrFolder: String = "return error",
        ifNoCurrentProject: String = "return error",
        ifNoPinnedProject: String = "return error",
    ) {
        fun toPrefValue(preference: String): ProjectActions.Preferences.PrefValue =
            when (preference) {
                "return error" -> ProjectActions.Preferences.PrefValue.ReturnError
                "notify caller" -> ProjectActions.Preferences.PrefValue.NotifyCaller
                else -> throw IllegalArgumentException("Unknown preference '$preference'")
            }

        koinAdd {
            single {
                ProjectActions.Preferences(
                    ifNoFileOrFolder = toPrefValue(ifNoFileOrFolder),
                    ifNoCurrentProject = toPrefValue(ifNoCurrentProject),
                    ifNoPinnedProject = toPrefValue(ifNoPinnedProject),
                )
            }
        }
    }

    fun HOTestCtx.`given 'caller notification' response is set`(
        noFolderResponse: String = "cancel",
        noFileResponse: String = "cancel",
        noCurrentProjectResponse: String = "cancel",
        noPinnedProjectResponse: String = "cancel",
    ) {
        fun toDecision(reaction: String): ProjectActions.CallerNotification.CallerDecision =
            when (reaction) {
                "cancel" -> ProjectActions.CallerNotification.CallerDecision.Cancel
                "continue" -> ProjectActions.CallerNotification.CallerDecision.Continue
                else -> throw IllegalArgumentException("Unknown reaction '$reaction'")
            }

        val obj = mock<ProjectActions.CallerNotification>(MockMode.autofill)
        everySuspend { obj.noFolder(any()) } returns toDecision(noFolderResponse)
        everySuspend { obj.noFile(any()) } returns toDecision(noFileResponse)
        everySuspend { obj.noCurrentProject() } returns toDecision(noCurrentProjectResponse)
        everySuspend { obj.noPinnedProject(any()) } returns toDecision(noPinnedProjectResponse)

        koinAdd {
            single<ProjectActions.CallerNotification> { obj }
        }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open folder in current project'`() {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openCurrentProjectFolder(
            actionPreferences = getActionPreferences(),
            callerNotification = getCallerNotification(),
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open file in current project'`(
        file: ProjectFile,
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openCurrentProjectFile(
            file = file,
            actionPreferences = getActionPreferences(),
            callerNotification = getCallerNotification(),
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open folder in pinned project'`(
        pinPosition: Int
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openPinnedProjectFolder(
            pinPosition = pinPosition,
            actionPreferences = getActionPreferences(),
            callerNotification = getCallerNotification(),
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open file in pinned project'`(
        pinPosition: Int,
        file: ProjectFile,
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openPinnedProjectFile(
            pinPosition = pinPosition,
            file = file,
            actionPreferences = getActionPreferences(),
            callerNotification = getCallerNotification(),
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open folder in regular project'`(
        projectId: Long
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openRegularProjectFolder(
            projectId = projectId,
            actionPreferences = getActionPreferences(),
            callerNotification = getCallerNotification(),
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open file in regular project'`(
        projectId: Long,
        file: ProjectFile,
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openRegularProjectFile(
            projectId = projectId,
            file = file,
            actionPreferences = getActionPreferences(),
            callerNotification = getCallerNotification(),
        )
        koinAdd { single { result } }
    }

    fun HOTestCtx.`then 'caller notification' receives`(notification: String) {
        val callerNotification: ProjectActions.CallerNotification = koin.get()

        when (notification) {
            "no file" -> {
                verifySuspend {
                    callerNotification.noFile(any())
                }
            }

            "no current project" -> {
                verifySuspend {
                    callerNotification.noCurrentProject()
                }
            }

            else -> throw IllegalArgumentException("Unknown notification '$notification'")
        }
    }

    private fun HOTestCtx.getActionPreferences(): ProjectActions.Preferences {
        val obj = runCatching { koin.get<ProjectActions.Preferences>() }
            .getOrNull()
            ?: ProjectActions.Preferences.CLI
        return obj
    }

    private fun HOTestCtx.getCallerNotification(): ProjectActions.CallerNotification {
        val obj = runCatching { koin.get<ProjectActions.CallerNotification>() }
            .getOrNull()
            ?: ProjectActions.CallerNotification.CancelAll
        return obj
    }
}
