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

    override suspend fun getRecommendedPath(inputParams: CreateProjectService.RecommendationCtx): FolderPath? {
        return when (inputParams) {
            CreateProjectService.RecommendationCtx.GlobalCtx -> null
            is CreateProjectService.RecommendationCtx.ProjectCtx ->
                projectRepository.getProject(inputParams.relatedProjectId)?.folderPath
        }
    }

    override suspend fun createProject(folder: String): CoreResult {
        val rawPath = folder.trim()
        if (rawPath.isBlank()) {
            return CoreResult.Error.Message("Project path is required.")
        }

        val folderPath = runCatching { FolderPath(rawPath.toPath()) }
            .getOrElse { return CoreResult.Error.Message("Project path is invalid.") }

        if (!fileSystemActions.pathExists(folderPath)) {
            return CoreResult.Error.FolderDoesNotExist(folderPath)
        }

        val normalizedPath = folderPath.path.toString()
        val projectAlreadyExists = runCatching {
            projectRepository.getAll()
                .first()
                .any { project -> project.folderPath.path.toString() == normalizedPath }
        }.getOrElse {
            return CoreResult.Error.Message("Unable to add project.")
        }

        if (projectAlreadyExists) {
            return CoreResult.Error.Message("Project already exists.")
        }

        return runCatching {
            projectRepository.addProject(folderPath)
            CoreResult.Success
        }.getOrElse {
            CoreResult.Error.Message("Unable to add project.")
        }
    }
}
