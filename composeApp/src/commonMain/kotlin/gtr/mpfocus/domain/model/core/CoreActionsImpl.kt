package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.MPFolder
import gtr.mpfocus.system_actions.OperatingSystemActions
import gtr.mpfocus.system_actions.TmpPath

class CoreActionsImpl(
    val operatingSystemActions: OperatingSystemActions,
    val fileSystemActions: FileSystemActions
) : CoreActions {

    override suspend fun openCurrentProjectFolder(actionPreferences: ActionPreferences, userInstructor: UserInstructor): ActionResult {
        // TEMPORARY IMPL - ONLY FOR SATISFYING TEST
        val folderPath = TmpPath("current-project")

        if (fileSystemActions.pathExists(folderPath)) {
            operatingSystemActions.openFolder(MPFolder(folderPath.path))
            return ActionResult.Success
        }

        return when (actionPreferences.ifNoFolder) {
            ActionPreferences.IfNoFileOrFolder.AutoCreate -> {
                val created = fileSystemActions.createFolder(folderPath)
                if (created) {
                    operatingSystemActions.openFolder(MPFolder(folderPath.path))
                    ActionResult.Success
                } else {
                    ActionResult.Error("failed to create folder")
                }
            }

            ActionPreferences.IfNoFileOrFolder.ReportError -> ActionResult.Error("folder doesn't exist")
            ActionPreferences.IfNoFileOrFolder.InstructUser -> {
                userInstructor.createFolder(folderPath.path)
                if (fileSystemActions.pathExists(folderPath)) {
                    operatingSystemActions.openFolder(MPFolder(folderPath.path))
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
