package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FolderPath

interface CreateProjectService {

    sealed interface RecommendationCtx {
        data class ProjectCtx(val relatedProjectId: Long) : RecommendationCtx
        data object GlobalCtx : RecommendationCtx
    }

    suspend fun getRecommendedPath(inputParams: RecommendationCtx): FolderPath?
    suspend fun createProject(folder: String, setAsCurrent: Boolean): CoreResult
}

