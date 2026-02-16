package gtr.mpfocus.domain.model.core

import gtr.common.distillText
import gtr.common.textFailure
import gtr.mpfocus.domain.model.config.ConfigService
import gtr.mpfocus.domain.model.core.ActionPreferences.IfNoFileOrFolder
import gtr.mpfocus.domain.model.repos.ProjectsRepo
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.system_actions.OperatingSystemActions
import kotlinx.coroutines.flow.first

class CoreActionsImpl(
    val operatingSystemActions: OperatingSystemActions,
    val fileSystemActions: FileSystemActions,
    val projectsRepo: ProjectsRepo,
    val configService: ConfigService,
) : CoreActions {

    internal suspend fun ensureCurrentProjectReady(
        userNotifier: UserNotifier = UserNotifier.None
    ): Result<Project> {
        val currentProject = projectsRepo.getCurrentProject().first()
        if (currentProject != null) {
            return Result.success(currentProject)
        }

        userNotifier.setCurrentProject()

        val updatedProject = projectsRepo.getCurrentProject().first()
        return if (updatedProject == null) {
            Result.textFailure("no current project")
        } else {
            Result.success(updatedProject)
        }
    }

    internal suspend fun ensurePinedProjectReady(
        pinPosition: Int,
        userNotifier: UserNotifier = UserNotifier.None
    ): Result<Project> {
        val pinnedProject = projectsRepo.getPinnedProjects()
            .first()
            .firstOrNull { it.pinPosition == pinPosition }

        if (pinnedProject != null) {
            return Result.success(pinnedProject)
        }

        userNotifier.setPinnedProject(pinPosition)

        val updatedPinnedProject = projectsRepo.getPinnedProjects()
            .first()
            .firstOrNull { it.pinPosition == pinPosition }

        return if (updatedPinnedProject == null) {
            Result.textFailure("no pinned project at position $pinPosition")
        } else {
            Result.success(updatedPinnedProject)
        }
    }

    internal suspend fun ensureProjectFileReady(
        project: Project,
        file: ProjectFiles,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): Result<FilePath> {
        val projectConfig = configService.getProjectConfig()
        val fileName = projectConfig.fileName(file)
        val filePath = FilePath(project.folderPath.path / fileName)

        if (fileSystemActions.pathExists(filePath)) {
            return Result.success(filePath)
        }

        return when (actionPreferences.ifNoFileOrFolder) {
            IfNoFileOrFolder.NotifyUser -> {
                userNotifier.createFile(filePath.path.toString())
                if (fileSystemActions.pathExists(filePath)) {
                    Result.success(filePath)
                } else {
                    Result.textFailure("no file exists")
                }
            }

            IfNoFileOrFolder.ReportError -> Result.textFailure("no file exists")
            IfNoFileOrFolder.AutoCreate -> {
                fileSystemActions.createFile(filePath)
                if (fileSystemActions.pathExists(filePath)) {
                    Result.success(filePath)
                } else {
                    Result.textFailure("no file exists")
                }
            }
        }
    }

    internal suspend fun ensureProjectFolderReady(
        project: Project,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): Result<FolderPath> {
        val folderPath = project.folderPath

        if (fileSystemActions.pathExists(folderPath)) {
            return Result.success(folderPath)
        }

        when (actionPreferences.ifNoFileOrFolder) {
            IfNoFileOrFolder.NotifyUser -> {
                userNotifier.createFolder(folderPath.path.toString())
            }

            IfNoFileOrFolder.ReportError -> {
                return Result.textFailure("no project folder")
            }

            IfNoFileOrFolder.AutoCreate -> {
                fileSystemActions.createFolder(folderPath)
            }
        }

        return if (fileSystemActions.pathExists(folderPath)) {
            Result.success(folderPath)
        } else {
            Result.textFailure("no project folder")
        }
    }

    override suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult {
        // todo: replace returning error with asking user 'set CP'
        val currentProject = projectsRepo.getCurrentProject().first()
            ?: return ActionResult.Error("no current project")

        val folderPath = currentProject.folderPath

        if (fileSystemActions.pathExists(folderPath)) {
            operatingSystemActions.openFolder(folderPath)
            return ActionResult.Success
        }

        return when (actionPreferences.ifNoFileOrFolder) {
            IfNoFileOrFolder.AutoCreate -> {
                val created = fileSystemActions.createFolder(folderPath)
                if (created) {
                    operatingSystemActions.openFolder(folderPath)
                    ActionResult.Success
                } else {
                    ActionResult.Error("failed to create folder")
                }
            }

            IfNoFileOrFolder.ReportError -> ActionResult.Error("folder doesn't exist")
            IfNoFileOrFolder.NotifyUser -> {
                userNotifier.createFolder(folderPath.path.toString())
                if (fileSystemActions.pathExists(folderPath)) {
                    operatingSystemActions.openFolder(folderPath)
                    ActionResult.Success
                } else {
                    ActionResult.Error("folder doesn't exist")
                }
            }
        }
    }


    override suspend fun openCurrentProjectFile(
        file: ProjectFiles,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult {
        val project = ensureCurrentProjectReady(userNotifier)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        ensureProjectFolderReady(project, actionPreferences, userNotifier)
            .onFailure { return ActionResult.Error(it.distillText()) }

        val projectFile = ensureProjectFileReady(project, file, actionPreferences, userNotifier)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        operatingSystemActions.openFile(projectFile)
        return ActionResult.Success
    }

    override suspend fun openPinnedProjectFolder(
        pinPosition: Int,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult {
        val project = ensurePinedProjectReady(pinPosition, userNotifier)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, userNotifier)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openPinnedProjectFile(
        pinPosition: Int,
        file: ProjectFiles,
        actionPreferences: ActionPreferences,
        userNotifier: UserNotifier
    ): ActionResult {
        val project = ensurePinedProjectReady(pinPosition, userNotifier)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        ensureProjectFolderReady(project, actionPreferences, userNotifier)
            .onFailure { return ActionResult.Error(it.distillText()) }

        val projectFile = ensureProjectFileReady(project, file, actionPreferences, userNotifier)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        operatingSystemActions.openFile(projectFile)
        return ActionResult.Success
    }
}
