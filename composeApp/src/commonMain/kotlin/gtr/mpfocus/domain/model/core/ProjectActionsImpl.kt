package gtr.mpfocus.domain.model.core

import gtr.common.distillText
import gtr.common.textFailure
import gtr.mpfocus.domain.model.core.ProjectActions.CallerNotification
import gtr.mpfocus.domain.model.core.ProjectActions.CallerNotification.NextStep
import gtr.mpfocus.domain.model.core.ProjectActions.Preferences
import gtr.mpfocus.domain.model.core.ProjectActions.Preferences.PrefValue
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.system_actions.OperatingSystemActions
import kotlinx.coroutines.flow.first

class ProjectActionsImpl(
    private val operatingSystemActions: OperatingSystemActions,
    private val fileSystemActions: FileSystemActions,
    private val projectRepository: ProjectRepository,
) : ProjectActions {

    internal suspend fun ensureCurrentProjectReady(
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): Result<Project> {
        val currentProject = projectRepository.getCurrentProject().first()
        if (currentProject != null) {
            return Result.success(currentProject)
        }

        return when (actionPreferences.ifNoCP) {
            PrefValue.ReturnError -> Result.textFailure("no current project")
            PrefValue.NotifyCaller -> {
                when (callerNotification.noCurrentProject()) {
                    NextStep.Cancel -> Result.textFailure("no current project")
                    NextStep.Continue -> {
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
                when (callerNotification.noFolder(folderPath.path.toString())) {
                    NextStep.Cancel -> return Result.textFailure("no project folder")
                    NextStep.Continue -> Unit
                }
            }
        }

        return if (fileSystemActions.pathExists(folderPath)) {
            Result.success(folderPath)
        } else {
            Result.textFailure("no project folder")
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

        return when (actionPreferences.ifNoPinned) {
            PrefValue.ReturnError -> Result.textFailure("no pinned project at position $pinPosition") // todo-soon: use 'text resources' to avoid text duplications like here 3x
            PrefValue.NotifyCaller -> {
                when (callerNotification.noPinnedProject(pinPosition)) {
                    NextStep.Cancel -> Result.textFailure("no pinned project at position $pinPosition")
                    NextStep.Continue -> {
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
            .getOrElse { return ActionResult.Error(it.distillText()) }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openCurrentProjectFile(
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openPinnedProjectFolder(
        pinPosition: Int,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensurePinnedProjectReady(pinPosition, actionPreferences, callerNotification)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openPinnedProjectFile(
        pinPosition: Int,
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    override suspend fun openRegularProjectFolder(
        projectId: Long,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult {
        val project = ensureRegularProjectReady(projectId)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        val folderPath = ensureProjectFolderReady(project, actionPreferences, callerNotification)
            .getOrElse { return ActionResult.Error(it.distillText()) }

        operatingSystemActions.openFolder(folderPath)
        return ActionResult.Success
    }

    override suspend fun openRegularProjectFile(
        projectId: Long,
        file: ProjectFile,
        actionPreferences: Preferences,
        callerNotification: CallerNotification
    ): ActionResult = ActionResult.Error(NOT_IMPLEMENTED_ERROR)

    private companion object {
        const val NOT_IMPLEMENTED_ERROR = "ProjectActions method is not implemented yet."
    }
}
