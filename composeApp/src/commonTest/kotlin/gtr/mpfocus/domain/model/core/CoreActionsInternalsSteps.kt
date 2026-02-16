package gtr.mpfocus.domain.model.core

import gtr.common.textFailure
import gtr.hotest.HOTestCtx
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FolderPath
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object CoreActionsInternalsSteps {

    suspend fun HOTestCtx.`when model executes 'ensure current project ready'`() {
        val coreActions: CoreActionsImpl = koin.get()
        val ui: UserNotifier = koin.get()

        val result = coreActions.ensureCurrentProjectReady(ui)

        koinAdd {
            single { result }
        }
    }

    suspend fun HOTestCtx.`when model executes 'ensure project file ready'`(
        file: ProjectFiles,
    ) {
        val coreActions: CoreActionsImpl = koin.get()
        val project: Project = koin.get()
        val aps: ActionPreferences = koin.get()
        val ui: UserNotifier = koin.get()

        val result = coreActions.ensureProjectFileReady(project, file, aps, ui)

        koinAdd {
            single { result }
        }
    }

    suspend fun HOTestCtx.`when model executes 'ensure project folder ready'`() {
        val coreActions: CoreActionsImpl = koin.get()
        val project: Project = koin.get()
        val aps: ActionPreferences = koin.get()
        val ui: UserNotifier = koin.get()

        val result = coreActions.ensureProjectFolderReady(project, aps, ui)

        koinAdd {
            single { result }
        }
    }

    fun HOTestCtx.`then model returns Result`(result: String) {
        val actual: Result<Project> = koin.get()
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
        val actual: Result<FilePath> = koin.get()
        assertTrue(actual.isSuccess)
        assertEquals(path, actual.getOrNull()!!.path.toString())
    }

    fun HOTestCtx.`then model returns folder path`(path: String) {
        val actual: Result<FolderPath> = koin.get()
        assertTrue(actual.isSuccess)
        assertEquals(path, actual.getOrNull()!!.path.toString())
    }
}
