package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FolderPath

interface CreateProjectService {

    sealed interface RecomCtx {
        data class ProjectCtx(val relatedProjectId: Long) : RecomCtx
        data object GlobalCtx : RecomCtx
    }

    // todo: move to global
    sealed interface Result {
        data object Success : Result
        data class Error(val message: String) : Result
    }

    suspend fun getRecommendedPath(inputParams: RecomCtx): FolderPath?
    suspend fun createProject(folder: String): Result
}

