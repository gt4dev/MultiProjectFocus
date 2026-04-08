package gtr.mpfocus.domain.repository

import dev.hotest.HOTestCtx
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import gtr.mpfocus.domain.model.core.Models
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.repository.Assertions.isTheSameProject
import gtr.mpfocus.infra.db_repo.ProjectRepositoryRoomImpl
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import okio.Path.Companion.toPath
import kotlin.test.assertTrue

object RepositorySteps {

    fun HOTestCtx.`then 'project repo' sets current project as`(projectId: Long) {
        val repository: ProjectRepository = koin.get()
        verifySuspend {
            repository.setCurrentProject(projectId)
        }
    }

    fun HOTestCtx.`given 'real project repo' exists`() {
        koinAdd {
            single { ProjectRepositoryRoomImpl(get()) }
            single<ProjectRepository> { get<ProjectRepositoryRoomImpl>() }
        }
    }

    suspend fun HOTestCtx.`then 'project repo' current project is`(
        project: Models.Project?,
    ) {
        val repo: ProjectRepository = koin.get()
        val cp = repo.getCurrentProject().first()
        assertTrue { isTheSameProject(cp, project) }
    }

    suspend fun HOTestCtx.`when 'project repo' sets current project`(
        id: Long?,
    ) {
        val repo: ProjectRepository = koin.get()
        repo.setCurrentProject(id)
    }


    fun HOTestCtx.`given 'project repository mock' returns current project`(
        currentProject: Models.Project? = null
    ) {
        val obj = initMockProjectsRepo()
        val proj =
            if (currentProject == null) null
            else Project(
                123,
                FolderPath(currentProject.path!!.toPath())
            )

        every { obj.getCurrentProject() } returns flowOf(proj)
    }

    fun HOTestCtx.`given 'project repository mock' sequentially returns current project`(
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

    fun HOTestCtx.`given 'project repository mock' returns pinned projects`(
        vararg pinnedProjects: Models.Project
    ) {
        val obj = initMockProjectsRepo()
        val projects = pinnedProjects.map {
            Project(
                it.id!!,
                FolderPath(it.path!!.toPath()),
                pinPosition = it.pinPosition,
            )
        }
        every { obj.getPinnedProjects() } returns flowOf(projects)
    }

    fun HOTestCtx.`given 'project repository mock' returns other projects`(
        vararg otherProjects: Models.Project
    ) {
        val obj = initMockProjectsRepo()
        val projects = otherProjects.map {
            Project(
                it.id!!,
                FolderPath(it.path!!.toPath()),
            )
        }
        every { obj.getOtherProjects() } returns flowOf(projects)
    }

    private fun HOTestCtx.initMockProjectsRepo(): ProjectRepository {
        val existing = koin.getOrNull<ProjectRepository>()
        if (existing != null) {
            return existing
        }

        val obj = mock<ProjectRepository>(MockMode.autofill) {
            every { getCurrentProject() } returns flowOf(null)
            every { getPinnedProjects() } returns flowOf(emptyList())
            every { getOtherProjects() } returns flowOf(emptyList())
        }
        koinAdd {
            single { obj }
        }
        return obj
    }
}
