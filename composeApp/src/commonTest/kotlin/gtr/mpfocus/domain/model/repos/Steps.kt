package gtr.mpfocus.domain.model.repos

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.flowOf
import okio.Path.Companion.toPath

object Steps {

    const val KEY_PROJECTS_REPO = "KEY_PROJECTS_REPO"

    // todo: add step verifying call to 'get current project'
    fun HOTestCtx.`given exists 'fake projects repo'`(
        currentProject: String? = null
    ) {
        if (this.containsKey(KEY_PROJECTS_REPO)) {
            return this[KEY_PROJECTS_REPO]
        }

        val obj = mock<ProjectsRepo>(MockMode.autofill) {
            every { getCurrentProject() } returns flowOf(null)
        }
        this[KEY_PROJECTS_REPO] = obj

        if (currentProject != null) {
            every { obj.getCurrentProject() } returns flowOf(
                Project(
                    123,
                    FolderPath(currentProject.toPath())
                )
            )
        }
    }
}
