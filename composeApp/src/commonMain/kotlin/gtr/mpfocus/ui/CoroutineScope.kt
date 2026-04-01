package gtr.mpfocus.ui

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus

fun CoroutineScope.createScopeWithExceptionHandler(
    onException: (Exception) -> Unit,
): CoroutineScope {

    val scopeExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is CancellationException -> Unit
            is Exception -> onException(throwable)
            else -> throw throwable
        }
    }

    return this + scopeExceptionHandler
}