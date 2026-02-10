package gtr.mpfocus.domain.model.core

import gtr.mpfocus.domain.model.repos.ProjectsRepo
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.OperatingSystemActions
import kotlinx.coroutines.flow.first

class CoreActionsImpl(
    val operatingSystemActions: OperatingSystemActions,
    val fileSystemActions: FileSystemActions,
    val projectsRepo: ProjectsRepo,
) : CoreActions {

    override suspend fun openCurrentProjectFolder(
        actionPreferences: ActionPreferences,
        userInstructor: UserInstructor
    ): ActionResult {
        // todo: replace returning error with asking user 'set CP'
        val currentProject = projectsRepo.getCurrentProject().first()
            ?: return ActionResult.Error("no current project")

        val folderPath = currentProject.folderPath

        if (fileSystemActions.pathExists(folderPath)) {
            operatingSystemActions.openFolder(folderPath)
            return ActionResult.Success
        }

        return when (actionPreferences.ifNoFolder) {
            ActionPreferences.IfNoFileOrFolder.AutoCreate -> {
                val created = fileSystemActions.createFolder(folderPath)
                if (created) {
                    operatingSystemActions.openFolder(folderPath)
                    ActionResult.Success
                } else {
                    ActionResult.Error("failed to create folder")
                }
            }

            ActionPreferences.IfNoFileOrFolder.ReportError -> ActionResult.Error("folder doesn't exist")
            ActionPreferences.IfNoFileOrFolder.InstructUser -> {
                userInstructor.createFolder(folderPath.path.toString())
                if (fileSystemActions.pathExists(folderPath)) {
                    operatingSystemActions.openFolder(folderPath)
                    ActionResult.Success
                } else {
                    ActionResult.Error("folder doesn't exist")
                }
            }
        }
    }

    override suspend fun openCurrentProjectFile(file: ProjectKnownFiles) {
        TODO("Not yet implemented")
    }

    override suspend fun openPinnedProjectFolder(pinPosition: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun openPinnedProjectFile(
        pinPosition: Int,
        file: ProjectKnownFiles
    ) {
        TODO("Not yet implemented")
    }
}
