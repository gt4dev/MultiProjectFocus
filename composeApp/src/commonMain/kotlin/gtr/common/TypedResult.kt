package gtr.common

sealed interface TypedResult<out Result> {
    data class Success<Result>(val value: Result) : TypedResult<Result>
    data class Error(val text: String) : TypedResult<Nothing>

    fun onError(body: (Error) -> Unit) {
        if (this is Error) {
            body(this)
        }
    }
}
