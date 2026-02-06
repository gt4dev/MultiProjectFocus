package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.OperatingSystemActions

class CoreActionsImpl(
    val operatingSystemActions: OperatingSystemActions,
    val fileSystemActions: FileSystemActions
) : CoreActions {

    override suspend fun openCurrentProjectFolder(actionPreferences: ActionPreferences) {
        TODO("Not yet implemented")
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