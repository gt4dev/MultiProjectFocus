package gtr.mpfocus.domain.model.core

import gtr.common.textFailure
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Steps.KEY_CORE_ACTIONS
import gtr.mpfocus.domain.model.core.Steps.KEY_USER_NOTIFIER
import gtr.mpfocus.system_actions.FilePath
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

    suspend fun HOTestCtx.`when model executes 'ensure project file ready'`(
        file: ProjectFiles,
    ) {
        val coreActions: CoreActionsImpl = this[KEY_CORE_ACTIONS]
        val aps: ActionPreferences = this[Steps.KEY_ACTION_PREFERENCES]
        val ui: UserNotifier = this[KEY_USER_NOTIFIER]

        val result = coreActions.ensureProjectFileReady(file, aps, ui)

        this[KEY_RESULT] = result
    }

    fun HOTestCtx.`then model returns Result`(result: String) {
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

    fun HOTestCtx.`then model returns file path`(path: String) {
        val actual: Result<FilePath> = this[KEY_RESULT]
        assertTrue(actual.isSuccess)
        assertEquals(path, actual.getOrNull()!!.path.toString())
    }
}
