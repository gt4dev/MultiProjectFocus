package gtr.mpfocus.ui.core

interface AppUi {
    suspend fun showMessage(msg: Message)
    suspend fun showMainWindow()
}

sealed interface Message {

    enum class Tone {
        Info,
        Error,
    }

    data class Text(
        val text: String,
        val tone: Tone = Tone.Info,
    ) : Message
}
