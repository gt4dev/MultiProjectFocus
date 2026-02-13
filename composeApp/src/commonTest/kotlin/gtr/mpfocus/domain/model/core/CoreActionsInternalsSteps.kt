package gtr.mpfocus.domain.model.core

import gtr.common.textFailure
import gtr.hotest.HOTestCtx
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FolderPath
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object CoreActionsInternalsSteps {

    suspend fun HOTestCtx.`when model executes 'assure current project ready'`() {
        val coreActions: CoreActionsImpl = this.koin.get()
        val ui: UserNotifier = this.koin.get()

        val result = coreActions.assureCurrentProjectReady(ui)

        this.addToKoinTestModule {
            single { result }
        }
    }

    suspend fun HOTestCtx.`when model executes 'ensure project file ready'`(
        file: ProjectFiles,
    ) {
        val coreActions: CoreActionsImpl = this.koin.get()
        val aps: ActionPreferences = this.koin.get()
        val ui: UserNotifier = this.koin.get()

        val result = coreActions.ensureProjectFileReady(file, aps, ui)

        this.addToKoinTestModule {
            single { result }
        }
    }

    suspend fun HOTestCtx.`when model executes 'ensure project folder ready'`() {
        val coreActions: CoreActionsImpl = this.koin.get()
        val aps: ActionPreferences = this.koin.get()
        val ui: UserNotifier = this.koin.get()

        val result = coreActions.ensureProjectFolderReady(aps, ui)

        this.addToKoinTestModule {
            single { result }
        }
    }

    fun HOTestCtx.`then model returns Result`(result: String) {
        val actual: Result<Project> = this.koin.get()
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
        val actual: Result<FilePath> = this.koin.get()
        assertTrue(actual.isSuccess)
        assertEquals(path, actual.getOrNull()!!.path.toString())
    }

    fun HOTestCtx.`then model returns folder path`(path: String) {
        val actual: Result<FolderPath> = this.koin.get()
        assertTrue(actual.isSuccess)
        assertEquals(path, actual.getOrNull()!!.path.toString())
    }
}
