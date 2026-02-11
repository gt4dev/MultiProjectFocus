package gtr.common

/**
 * semi-exception useful for passing 'text' to the caller in Result wrapper
 * eg: `return Result.failure(TextBearingException("some message to upper layers"))`
 */
data class TextBearingException(val text: String) : Throwable()

fun <T> Result.Companion.textFailure(text: String): Result<T> {
    return Result.failure(TextBearingException(text))
}

fun Throwable.distillText(): String = when (this) {
    is TextBearingException -> text
    else -> "[text not found]"
}
