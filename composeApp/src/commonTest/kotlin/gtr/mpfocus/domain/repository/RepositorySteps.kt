package gtr.mpfocus.domain.repository

import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.repository.Assertions.isTheSameProject
import gtr.mpfocus.infra.db_repo.ProjectRepositoryRoomImpl
import kotlinx.coroutines.flow.first
import kotlin.test.assertTrue

object RepositorySteps {

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
}
