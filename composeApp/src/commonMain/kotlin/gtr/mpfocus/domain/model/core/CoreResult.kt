package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FolderPath

sealed interface CoreResult {
    data object Success : CoreResult

    sealed interface Error : CoreResult {
        data class FolderDoesNotExist(val path: FolderPath) : Error
        data class Message(val msg: String) : Error
    }
}