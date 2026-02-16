package gtr.mpfocus.domain.model.core

import dev.mokkery.MockMode
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import gtr.hotest.HOTestCtx
import gtr.mpfocus.system_actions.FolderPath
import okio.Path.Companion.toPath
import kotlin.test.assertEquals

object Steps {

    fun HOTestCtx.`given exists 'action preferences'`(withIfNoFolder: String) {

        val ifNoFolder: ActionPreferences.IfNoFileOrFolder = when (withIfNoFolder) {
            "auto create" -> ActionPreferences.IfNoFileOrFolder.AutoCreate
            "report error" -> ActionPreferences.IfNoFileOrFolder.ReportError
            "notify user" -> ActionPreferences.IfNoFileOrFolder.NotifyUser
            else -> throw IllegalArgumentException("Unknown preference $withIfNoFolder")
        }

        koinAdd {
            single {
                ActionPreferences(
                    ifNoFileOrFolder = ifNoFolder
                )
            }
        }
    }

    fun HOTestCtx.`given 'sample project' has`(folderPath: String) {
        koinAdd {
            single {
                Project(
                    projectId = 123,
                    folderPath = FolderPath(folderPath.toPath())
                )
            }
        }
    }

    fun HOTestCtx.`given exists 'real model'`() {
        koinAdd {
            single {
                CoreActionsImpl(
                    get(),
                    get(),
                    get(),
                    get(),
                )
            }
            single<CoreActions> { get<CoreActionsImpl>() }
        }
    }

    suspend fun HOTestCtx.`when model executes command 'open folder in current project'`() {
        val coreActions: CoreActions = koin.get()
        val aps: ActionPreferences = koin.get()
        val ui: UserNotifier = koin.get()

        val result = coreActions.openCurrentProjectFolder(aps, ui)

        koinAdd {
            single { result }
        }
    }

    suspend fun HOTestCtx.`when model executes command 'open file in current project'`(
        file: ProjectFiles,
    ) {
        val coreActions: CoreActions = koin.get()
        val aps: ActionPreferences = koin.get()
        val ui: UserNotifier = koin.get()

        val result = coreActions.openCurrentProjectFile(file, aps, ui)

        koinAdd {
            single { result }
        }
    }

    suspend fun HOTestCtx.`when model executes command 'open folder in pinned project'`(
        pinPosition: Int,
    ) {
        val coreActions: CoreActions = koin.get()
        val aps: ActionPreferences = koin.get()
        val ui: UserNotifier = koin.get()

        val result = coreActions.openPinnedProjectFolder(pinPosition, aps, ui)

        koinAdd {
            single { result }
        }
    }

    fun HOTestCtx.`then model returns`(result: String) {
        val expected = when {
            result == "success" -> ActionResult.Success
            result.startsWith("error: ") -> {
                val msg = result.removePrefix("error: ")
                ActionResult.Error(msg)
            }

            else -> throw IllegalArgumentException("Unknown result '$result'")
        }
        val actual: ActionResult = koin.get()
        assertEquals(expected, actual)
    }

    fun HOTestCtx.`given exists 'fake user notifier'`() {
        val obj = mock<UserNotifier>(MockMode.autofill) // create default impl of interface
        koinAdd {
            single { obj }
        }
    }

    fun HOTestCtx.`then model notify user to`(what: String) {
        val obj: UserNotifier = koin.get()
        when (what) {
            "create folder" -> {
                verifySuspend {
                    obj.createFolder(any())
                }
            }

            "set current project" -> {
                verifySuspend {
                    obj.setCurrentProject()
                }
            }

            "set pinned project" -> {
                verifySuspend {
                    obj.setPinnedProject(any())
                }
            }

            "create file" -> {
                verifySuspend {
                    obj.createFile(any())
                }
            }

            else -> throw IllegalArgumentException("unknown option: $what")
        }
    }
}
