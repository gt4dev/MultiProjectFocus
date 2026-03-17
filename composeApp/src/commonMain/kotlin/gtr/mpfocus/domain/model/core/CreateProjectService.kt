package gtr.mpfocus.domain.model.core

interface CreateProjectService {

    // todo: move to global
    sealed interface Result {
        data object Success : Result
        data class Error(val message: String) : Result
    }

    suspend fun createProject(folder: String): Result
}

