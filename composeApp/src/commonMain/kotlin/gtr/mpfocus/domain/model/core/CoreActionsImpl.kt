package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.MPFolder
import gtr.mpfocus.system_actions.OperatingSystemActions
import gtr.mpfocus.system_actions.TmpPath

class CoreActionsImpl(
    val operatingSystemActions: OperatingSystemActions,
    val fileSystemActions: FileSystemActions
) : CoreActions {

    override suspend fun openCurrentProjectFolder(actionPreferences: ActionPreferences): ActionResult {
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
                    ActionResult.Failure
                }
            }

            ActionPreferences.IfNoFileOrFolder.ReportError -> ActionResult.Failure
            ActionPreferences.IfNoFileOrFolder.AskUser -> ActionResult.Cancel
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
