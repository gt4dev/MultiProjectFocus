package gtr.mpfocus.domain.model

interface ProjectActions {

    /**
     * adr: returning result from operation
     * - for now it's not needed - opers return Unit
     * - maybe later it be useful to - either:
     *  - return Result<Error> to show problem to user
     *  - sealed class Result { object Success, class Error(details) }
     */
    fun openCurrentProjectFile(file: ProjectKnownFiles)

    fun openCurrentProjectFolder()

    fun openPinnedProjectFile(pinPosition: Int, file: ProjectKnownFiles)

    fun openPinnedProjectFolder(pinPosition: Int)
}

// todo: impl
// class ProjectActionsImpl : ProjectActions