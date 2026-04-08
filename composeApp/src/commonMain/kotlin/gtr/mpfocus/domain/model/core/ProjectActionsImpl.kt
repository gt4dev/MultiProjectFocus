package gtr.mpfocus.domain.model.core

import gtr.common.distillText
import gtr.common.textFailure
import gtr.mpfocus.domain.model.config.ProjectConfigService
import gtr.mpfocus.domain.model.core.ProjectActions.CallerNotification
import gtr.mpfocus.domain.model.core.ProjectActions.CallerNotification.CallerDecision
import gtr.mpfocus.domain.model.core.ProjectActions.Preferences
import gtr.mpfocus.domain.model.core.ProjectActions.Preferences.PrefValue
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.system_actions.OperatingSystemActions
import kotlinx.coroutines.flow.first
import okio.Path.Companion.toPath


data class NoFileException(
    val filePath: FilePath,
) : Throwable()


class ProjectActionsImpl(
    private val operatingSystemActions: OperatingSystemActions,
    private val fileSystemActions: FileSystemActions,
    private val projectRepository: ProjectRepository,
    private val configService: ProjectConfigService,
) : ProjectActions {

    internal suspend fun ensureCurrentProjectReady(
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): Result<Project> {
        val currentProject = projectRepository.getCurrentProject().first()
        if (currentProject != null) {
            return Result.success(currentProject)
        }

        return when (actionPreferences.ifNoCurrentProject) {
            PrefValue.ReturnError -> Result.textFailure("no current project")
            PrefValue.NotifyCaller -> {
                when (callerNotification.noCurrentProject()) {
                    CallerDecision.Cancel -> Result.textFailure("no current project")
                    CallerDecision.Continue -> {
                        val updatedProject = projectRepository.getCurrentProject().first()
                        if (updatedProject == null) {
                            Result.textFailure("no current project")
                        } else {
                            Result.success(updatedProject)
                        }
                    }
                }
            }
        }
    }

    internal suspend fun ensureProjectFolderReady(
        project: Project,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): Result<FolderPath> {
        val folderPath = project.folderPath
        if (fileSystemActions.pathExists(folderPath)) {
            return Result.success(folderPath)
        }

        when (actionPreferences.ifNoFileOrFolder) {
            PrefValue.ReturnError -> return Result.textFailure("no project folder")
            PrefValue.NotifyCaller -> {
                when (callerNotification.noFolder()) {
                    CallerDecision.Cancel -> return Result.textFailure("no project folder")
                    CallerDecision.Continue -> Unit
                }
            }
        }

        return if (fileSystemActions.pathExists(folderPath)) {
            Result.success(folderPath)
        } else {
            Result.textFailure("no project folder")
        }
    }

    internal suspend fun ensureProjectFileReady(
        project: Project,
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): Result<FilePath> {
        val projectConfig = configService.getLocalProjectConfig(project.folderPath)
        val fileName = projectConfig.fileName(file)
        val filePath = FilePath("${project.folderPath.path}/$fileName".toPath())
        if (fileSystemActions.pathExists(filePath)) {
            return Result.success(filePath)
        }

        when (actionPreferences.ifNoFileOrFolder) {
            PrefValue.ReturnError -> return Result.failure(NoFileException(filePath))
            PrefValue.NotifyCaller -> {
                when (callerNotification.noFile()) {
                    CallerDecision.Cancel -> return Result.failure(NoFileException(filePath))
                    CallerDecision.Continue -> Unit
                }
            }
        }

        return if (fileSystemActions.pathExists(filePath)) {
            Result.success(filePath)
        } else {
            Result.failure(NoFileException(filePath))
        }
    }

    internal suspend fun ensurePinnedProjectReady(
        pinPosition: Int,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): Result<Project> {
        val pinnedProject = projectRepository.getPinnedProjects()
            .first()
            .firstOrNull { it.pinPosition == pinPosition }

        if (pinnedProject != null) {
            return Result.success(pinnedProject)
        }

        return when (actionPreferences.ifNoPinnedProject) {
            PrefValue.ReturnError -> Result.textFailure("no pinned project at position $pinPosition") // todo-soon: use 'text resources' to avoid text duplications like here 3x
            PrefValue.NotifyCaller -> {
                when (callerNotification.noPinnedProject()) {
                    CallerDecision.Cancel -> Result.textFailure("no pinned project at position $pinPosition")
                    CallerDecision.Continue -> {
                        val updatedPinnedProject = projectRepository.getPinnedProjects()
                            .first()
                            .firstOrNull { it.pinPosition == pinPosition }

                        if (updatedPinnedProject == null) {
                            Result.textFailure("no pinned project at position $pinPosition")
                        } else {
                            Result.success(updatedPinnedProject)
                        }
                    }
                }
            }
        }
    }

    internal suspend fun ensureRegularProjectReady(
        projectId: Long,
    ): Result<Project> {
        val regularProject = projectRepository.getOtherProjects()
            .first()
            .firstOrNull { it.projectId == projectId }

        return if (regularProject == null) {
            Result.textFailure("no regular project with id $projectId")
        } else {
            Result.success(regularProject)
        }
    }

    override suspend fun openCurrentProjectFolder(
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensureCurrentProjectReady(actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openCurrentProjectFile(
        fileId: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensureCurrentProjectReady(actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .onFailure { return it.toActionResult() }

        val projectFile =
            ensureProjectFileReady(project, fileId, actionPreferences, callerNotification)
                .getOrElse { return it.toActionResult() }

        operatingSystemActions.openFile(projectFile)
        return ActionResult.Success
    }

    override suspend fun openPinnedProjectFolder(
        pinPosition: Int,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensurePinnedProjectReady(pinPosition, actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openPinnedProjectFile(
        pinPosition: Int,
        fileId: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensurePinnedProjectReady(pinPosition, actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .onFailure { return it.toActionResult() }

        val projectFile =
            ensureProjectFileReady(project, fileId, actionPreferences, callerNotification)
                .getOrElse { return it.toActionResult() }

        operatingSystemActions.openFile(projectFile)
        return ActionResult.Success
    }

    override suspend fun openRegularProjectFolder(
        projectId: Long,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensureRegularProjectReady(projectId)
            .getOrElse { return it.toActionResult() }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .getOrElse { return it.toActionResult() }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openRegularProjectFile(
        projectId: Long,
        fileId: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensureRegularProjectReady(projectId)
            .getOrElse { return it.toActionResult() }

        ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .onFailure { return it.toActionResult() }

        val projectFile =
            ensureProjectFileReady(project, fileId, actionPreferences, callerNotification)
                .getOrElse { return it.toActionResult() }

        operatingSystemActions.openFile(projectFile)
        return ActionResult.Success
    }

    private fun Throwable.toActionResult(): ActionResult {
        return when (this) {
            is NoFileException -> ActionResult.NoFileError
            else -> ActionResult.Error(distillText())
        }
    }
}
