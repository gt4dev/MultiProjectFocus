package gtr.mpfocus.domain.model.core

import gtr.common.textFailure
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Steps.KEY_CORE_ACTIONS
import gtr.mpfocus.domain.model.core.Steps.KEY_USER_NOTIFIER
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object CoreActionsInternalsSteps {

    private const val KEY_RESULT = "KEY_RESULT"

    suspend fun HOTestCtx.`when model executes 'assure current project ready'`() {
        val coreActions: CoreActionsImpl = this[KEY_CORE_ACTIONS]
        val ui: UserNotifier = this[KEY_USER_NOTIFIER]
        val result = coreActions.assureCurrentProjectReady(ui)
        this[KEY_RESULT] = result
    }

    fun HOTestCtx.`then model returns typed result`(result: String) {
        val actual: Result<Project> = this[KEY_RESULT]
        when {
            result == "success" -> assertTrue(actual.isSuccess)
            result.startsWith("error: ") -> {
                val msg = result.removePrefix("error: ")
                assertEquals(Result.textFailure(msg), actual)
            }

            else -> throw IllegalArgumentException("Unknown result '$result'")
        }
    }
}
