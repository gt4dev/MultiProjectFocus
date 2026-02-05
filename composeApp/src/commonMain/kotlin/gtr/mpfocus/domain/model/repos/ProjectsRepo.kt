package gtr.mpfocus.domain.model.repos

import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.model.core.ProjectId
import kotlinx.coroutines.flow.Flow

interface ProjectsRepo {

    fun getCurrentProject(): Flow<Project>
    suspend fun setCurrentProject(pid: ProjectId)

    fun getPinnedProjects(): Flow<List<Project>>
    suspend fun clearAllPins()
    suspend fun pinProject(projectId: ProjectId, pinPosition: Int)

    fun getLastUsedProjects(): Flow<List<Project>>
}