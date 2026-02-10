package gtr.mpfocus.domain.model.repos

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.mock
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.flowOf
import okio.Path.Companion.toPath

object Steps {

    const val KEY_PROJECTS_REPO = "KEY_PROJECTS_REPO"

    fun HOTestCtx.`given exists 'fake projects repo'`(
        currentProject: String? = null
    ) {
        val obj = initMockProjectsRepo()
        if (currentProject != null) {
            every { obj.getCurrentProject() } returns flowOf(
                Project(
                    123,
                    FolderPath(currentProject.toPath())
                )
            )
        }
    }

    fun HOTestCtx.`given 'fake projects repo' returns current project`(
        vararg currentProjects: String?,
    ) {
        val obj = initMockProjectsRepo()
        val flows = currentProjects.map { currentProject ->
            if (currentProject == null) {
                flowOf(null)
            } else {
                flowOf(
                    Project(
                        123,
                        FolderPath(currentProject.toPath())
                    )
                )
            }
        }
        every { obj.getCurrentProject() } sequentiallyReturns flows
    }

    private fun HOTestCtx.initMockProjectsRepo(): ProjectsRepo {
        if (this.containsKey(KEY_PROJECTS_REPO)) {
            return this[KEY_PROJECTS_REPO]
        }

        val obj = mock<ProjectsRepo>(MockMode.autofill) {
            every { getCurrentProject() } returns flowOf(null)
        }
        this[KEY_PROJECTS_REPO] = obj
        return obj
    }
}
