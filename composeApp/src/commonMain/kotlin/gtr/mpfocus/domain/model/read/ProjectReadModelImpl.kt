package gtr.mpfocus.domain.model.read

import gtr.mpfocus.domain.model.config.ProjectConfig
import gtr.mpfocus.domain.model.config.ProjectConfigService
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectReadModelImpl(
    private val projectRepository: ProjectRepository,
    private val projectConfigService: ProjectConfigService,
) : ProjectReadModel {

    override fun getCurrentProject(): Flow<ProjectWithFileNames?> {
        return projectRepository.getCurrentProject().flatMapLatest { project ->
            if (project == null) {
                flowOf(null)
            } else {
                buildProjectWithFileNames(project)
            }
        }
    }

    override fun getPinnedProjects(): Flow<List<ProjectWithFileNames>> {
        return projectRepository.getPinnedProjects().flatMapLatest(::buildProjectsWithFileNames)
    }

    override fun getRegularProjects(): Flow<List<ProjectWithFileNames>> {
        return projectRepository.getOtherProjects().flatMapLatest(::buildProjectsWithFileNames)
    }

    private fun buildProjectsWithFileNames(projects: List<Project>): Flow<List<ProjectWithFileNames>> = flow {
        emit(projects.map { toProjectWithFileNames(it, null) })

        val resolvedConfigs = mutableMapOf<FolderPath, ProjectConfig>()
        projects.forEach { project ->
            resolvedConfigs[project.folderPath] = projectConfigService.getProjectConfig(project.folderPath)
            emit(
                projects.map { candidate ->
                    toProjectWithFileNames(
                        candidate,
                        projectConfig = resolvedConfigs[candidate.folderPath],
                    )
                }
            )
        }
    }

    private fun buildProjectWithFileNames(project: Project) = flow {
        emit(toProjectWithFileNames(project, null))
        emit(
            toProjectWithFileNames(
                project,
                projectConfig = projectConfigService.getProjectConfig(project.folderPath),
            )
        )
    }

    private fun toProjectWithFileNames(project: Project, projectConfig: ProjectConfig?): ProjectWithFileNames {
        val namedFiles = ProjectFile.entries.map { file ->
            FileName(
                fileId = file,
                fileName = projectConfig?.fileName(file) ?: "${file.name}.md",
            )
        }

        return ProjectWithFileNames(
            project = project,
            fileNames = namedFiles,
        )
    }
}
