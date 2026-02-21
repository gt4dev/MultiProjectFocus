package gtr.mpfocus.infra.db_repo

import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ProjectRepositoryRoomImpl(
    private val projectDao: ProjectDao
) : ProjectRepository {

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            projectDao.deleteAll()
        }
    }

    override fun getCurrentProject(): Flow<Project?> {
        return projectDao.getCurrentProject().map { it?.toDomain() }
    }

    override suspend fun setCurrentProject(projectId: Long?) {
        withContext(Dispatchers.IO) {
            projectDao.clearCurrentProject()
            if (projectId != null) {
                projectDao.setCurrentProject(projectId)
            }
        }
    }

    override fun getPinnedProjects(): Flow<List<Project>> {
        return projectDao.getPinnedProjects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun clearAllPins() {
        withContext(Dispatchers.IO) {
            projectDao.clearAllPins()
        }
    }

    override suspend fun pinProject(projectId: Long, pinPosition: Int) {
        withContext(Dispatchers.IO) {
            projectDao.pinProject(projectId, pinPosition)
        }
    }

    override fun getAll(): Flow<List<Project>> {
        return projectDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addProject(projectPath: FolderPath): Long {
        return withContext(Dispatchers.IO) {
            projectDao.insert(
                ProjectEntity(
                    folderPath = projectPath.path.toString(),
                    isCurrent = false,
                    pinPosition = null
                )
            )
        }
    }
}
