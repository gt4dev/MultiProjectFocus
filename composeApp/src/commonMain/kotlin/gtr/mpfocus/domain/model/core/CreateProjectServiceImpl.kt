package gtr.mpfocus.domain.model.core

import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.first
import okio.Path.Companion.toPath

class CreateProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val fileSystemActions: FileSystemActions,
) : CreateProjectService {

    override suspend fun getRecommendedPath(inputParams: CreateProjectService.RecomCtx): FolderPath? {
        return when (inputParams) {
            CreateProjectService.RecomCtx.GlobalCtx -> null
            is CreateProjectService.RecomCtx.ProjectCtx ->
                projectRepository.getProject(inputParams.relatedProjectId)?.folderPath
        }
    }

    override suspend fun createProject(folder: String): CreateProjectService.Result {
        val rawPath = folder.trim()
        if (rawPath.isBlank()) {
            return CreateProjectService.Result.Error("Project path is required.")
        }

        val folderPath = runCatching { FolderPath(rawPath.toPath()) }
            .getOrElse { return CreateProjectService.Result.Error("Project path is invalid.") }

        if (!fileSystemActions.pathExists(folderPath)) {
            return CreateProjectService.Result.Error("Project folder does not exist.")
        }

        val normalizedPath = folderPath.path.toString()
        val projectAlreadyExists = runCatching {
            projectRepository.getAll()
                .first()
                .any { project -> project.folderPath.path.toString() == normalizedPath }
        }.getOrElse {
            return CreateProjectService.Result.Error("Unable to create project.")
        }

        if (projectAlreadyExists) {
            return CreateProjectService.Result.Error("Project already exists.")
        }

        return runCatching {
            projectRepository.addProject(folderPath)
            CreateProjectService.Result.Success
        }.getOrElse {
            CreateProjectService.Result.Error("Unable to create project.")
        }
    }
}
