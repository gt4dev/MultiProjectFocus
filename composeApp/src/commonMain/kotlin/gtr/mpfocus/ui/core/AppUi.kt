package gtr.mpfocus.ui.core

interface AppUi {
    suspend fun showMessage(msg: Message)
}

interface Message

data class TextMessage(val text: String) : Message