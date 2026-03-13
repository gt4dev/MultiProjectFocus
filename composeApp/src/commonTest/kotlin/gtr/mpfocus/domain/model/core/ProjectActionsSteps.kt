package gtr.mpfocus.domain.model.core

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import gtr.hotest.HOTestCtx

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
            single { ProjectActionsImpl(get(), get(), get()) }
            single<ProjectActions> { get<ProjectActionsImpl>() }
        }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open folder in current project'`() {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openCurrentProjectFolder(
            actionPreferences = ProjectActions.Preferences.CLI,
            callerNotification = ProjectActions.CallerNotification.CancelAll
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open folder in pinned project'`(
        pinPosition: Int
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openPinnedProjectFolder(
            pinPosition = pinPosition,
            actionPreferences = ProjectActions.Preferences.CLI,
            callerNotification = ProjectActions.CallerNotification.CancelAll
        )
        koinAdd { single { result } }
    }

    suspend fun HOTestCtx.`when 'project actions' executes command 'open folder in regular project'`(
        projectId: Long
    ) {
        val projectActions: ProjectActions = koin.get()
        val result = projectActions.openRegularProjectFolder(
            projectId = projectId,
            actionPreferences = ProjectActions.Preferences.CLI,
            callerNotification = ProjectActions.CallerNotification.CancelAll
        )
        koinAdd { single { result } }
    }
}