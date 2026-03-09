package gtr.mpfocus.domain.model.core

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import gtr.hotest.HOTestCtx

object ProjectActionsMockSteps {

    fun HOTestCtx.`given 'fake project actions' exists`() {
        val obj = mock<ProjectActions>(MockMode.autofill)

        // todo-soon: move to particular mock setting steps
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

}