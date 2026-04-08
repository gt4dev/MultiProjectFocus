package gtr.mpfocus.domain.model.read

import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.model.core.ProjectFile
import kotlinx.coroutines.flow.Flow


data class FileName(
    val fileId: ProjectFile,
    val fileName: String,
)

data class ProjectWithFileNames(
    val project: Project,
    val fileNames: List<FileName>,
)


interface ProjectReadModel {
    fun getCurrentProject(): Flow<ProjectWithFileNames?>
    fun getPinnedProjects(): Flow<List<ProjectWithFileNames>>
    fun getRegularProjects(): Flow<List<ProjectWithFileNames>>
}
