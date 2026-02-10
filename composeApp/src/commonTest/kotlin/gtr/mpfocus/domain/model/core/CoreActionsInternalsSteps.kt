package gtr.mpfocus.domain.model.core

import gtr.common.TypedResult
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Steps.KEY_CORE_ACTIONS
import gtr.mpfocus.domain.model.core.Steps.KEY_USER_INSTRUCTOR
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object CoreActionsInternalsSteps {

    private const val KEY_RESULT = "KEY_RESULT"

    suspend fun HOTestCtx.`when model executes 'assure current project ready'`() {
        val coreActions: CoreActionsImpl = this[KEY_CORE_ACTIONS]
        val ui: UserInstructor = this[KEY_USER_INSTRUCTOR]
        val result = coreActions.assureCurrentProjectReady(ui)
        this[KEY_RESULT] = result
    }

    fun HOTestCtx.`then model returns typed result`(result: String) {
        val actual: TypedResult<Project> = this[KEY_RESULT]
        when {
            result == "success" -> assertTrue(actual is TypedResult.Success<*>)
            result.startsWith("error: ") -> {
                val msg = result.removePrefix("error: ")
                assertEquals(TypedResult.Error(msg), actual)
            }

            else -> throw IllegalArgumentException("Unknown result '$result'")
        }
    }
}
