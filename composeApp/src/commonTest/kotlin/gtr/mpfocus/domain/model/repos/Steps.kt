package gtr.mpfocus.domain.model.repos

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.mock
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Models
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.flowOf
import okio.Path.Companion.toPath

object Steps {

    fun HOTestCtx.`given exists 'fake projects repo'`(
        withCurrentProject: String? = null
    ) {
        val obj = initMockProjectsRepo()
        if (withCurrentProject != null) {
            every { obj.getCurrentProject() } returns flowOf(
                Project(
                    123,
                    FolderPath(withCurrentProject.toPath())
                )
            )
        }
    }

    fun HOTestCtx.`given exists 'fake projects repo'`(
        vararg withSequentialCurrentProject: String?,
    ) {
        val obj = initMockProjectsRepo()
        val flows = withSequentialCurrentProject.map { currentProject ->
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

    fun HOTestCtx.`given 'fake projects repo' returns pinned project`(
        pinnedProject: Models.PinnedProject?
    ) {
        val obj = initMockProjectsRepo()
        val pinnedProjects = if (pinnedProject == null) {
            emptyList()
        } else
            listOf(
                Project(
                    123,
                    FolderPath(pinnedProject.folderPath.toPath()),
                    pinPosition = pinnedProject.pinPosition,
                )
            )
        every { obj.getPinnedProjects() } returns flowOf(pinnedProjects)
    }

    fun HOTestCtx.`given 'fake projects repo' returns sequential pinned project`(
        vararg sequentialPinnedProject: Models.PinnedProject?,
    ) {
        val obj = initMockProjectsRepo()
        val flows = sequentialPinnedProject.map { pinnedProject ->
            if (pinnedProject == null) {
                flowOf(emptyList())
            } else {
                flowOf(
                    listOf(
                        Project(
                            123,
                            FolderPath(pinnedProject.folderPath.toPath()),
                            pinPosition = pinnedProject.pinPosition,
                        )
                    )
                )
            }
        }
        every { obj.getPinnedProjects() } sequentiallyReturns flows
    }

    private fun HOTestCtx.initMockProjectsRepo(): ProjectRepository {
        val existing = runCatching { koin.get<ProjectRepository>() }.getOrNull()
        if (existing != null) {
            return existing
        }

        val obj = mock<ProjectRepository>(MockMode.autofill) {
            every { getCurrentProject() } returns flowOf(null)
            every { getPinnedProjects() } returns flowOf(emptyList())
        }
        koinAdd {
            single { obj }
        }
        return obj
    }
}
