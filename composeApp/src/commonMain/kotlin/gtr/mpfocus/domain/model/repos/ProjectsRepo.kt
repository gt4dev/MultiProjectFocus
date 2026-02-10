package gtr.mpfocus.domain.model.repos

import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.Path.Companion.toPath

interface ProjectsRepo {
    fun getCurrentProject(): Flow<Project?>
    suspend fun setCurrentProject(projectId: Long)

    fun getPinnedProjects(): Flow<List<Project>>
    suspend fun clearAllPins()
    suspend fun pinProject(projectId: Long, pinPosition: Int)

    fun getLastUsedProjects(): Flow<List<Project>>
}


class DevTimeProjectsRepoImpl(val hasCP: Boolean) : ProjectsRepo {

    override fun getCurrentProject(): Flow<Project?> = flow {
        if (hasCP) {
            emit(
                Project(
                    123,
                    FolderPath("c:/hello123/bla/baa/123".toPath()),
                )
            )
        } else {
            emit(null)
        }
    }

    override suspend fun setCurrentProject(projectId: Long) {
        TODO("Not yet implemented")
    }

    override fun getPinnedProjects(): Flow<List<Project>> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAllPins() {
        TODO("Not yet implemented")
    }

    override suspend fun pinProject(projectId: Long, pinPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun getLastUsedProjects(): Flow<List<Project>> {
        TODO("Not yet implemented")
    }

}
