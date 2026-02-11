package gtr.mpfocus.domain.model.core

import dev.mokkery.MockMode
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import gtr.hotest.HOTestCtx
import kotlin.test.assertEquals
import gtr.mpfocus.domain.model.repos.Steps as ProjectsRepoSteps
import gtr.mpfocus.system_actions.Steps as FileSystemActionsSteps

object Steps {

    const val KEY_ACTION_PREFERENCES = "KEY_ACTION_PREFERENCES"
    const val KEY_CORE_ACTIONS = "KEY_CORE_ACTIONS"
    const val KEY_CORE_ACTIONS_RESULT = "KEY_CORE_ACTIONS_RESULT"
    const val KEY_USER_NOTIFIER = "KEY_USER_NOTIFIER"

    fun HOTestCtx.`given action preference 'if no folder' is`(pref: String) {

        val ifNoFolder: ActionPreferences.IfNoFileOrFolder = when (pref) {
            "auto create" -> ActionPreferences.IfNoFileOrFolder.AutoCreate
            "report error" -> ActionPreferences.IfNoFileOrFolder.ReportError
            "notify user" -> ActionPreferences.IfNoFileOrFolder.NotifyUser
            else -> throw IllegalArgumentException("Unknown preference $pref")
        }

        this[KEY_ACTION_PREFERENCES] = ActionPreferences(
            ifNoFileOrFolder = ifNoFolder
        )
    }

    fun HOTestCtx.`given exists 'real model'`() {
        this[KEY_CORE_ACTIONS] = CoreActionsImpl(
            this[FileSystemActionsSteps.KEY_OPERATING_SYSTEM_ACTIONS],
            this[FileSystemActionsSteps.KEY_FILE_SYSTEM_ACTIONS],
            this[ProjectsRepoSteps.KEY_PROJECTS_REPO],
        )
    }

    suspend fun HOTestCtx.`when model executes command 'open folder in current project'`() {
        val coreActions: CoreActions = this[KEY_CORE_ACTIONS]
        val aps: ActionPreferences = this[KEY_ACTION_PREFERENCES]
        val ui: UserNotifier = this[KEY_USER_NOTIFIER]
        val result = coreActions.openCurrentProjectFolder(aps, ui)
        this[KEY_CORE_ACTIONS_RESULT] = result
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
        assertEquals(expected, this[KEY_CORE_ACTIONS_RESULT])
    }

    fun HOTestCtx.`given exists 'fake user notifier'`() {
        if (this.containsKey(KEY_USER_NOTIFIER)) {
            return this[KEY_USER_NOTIFIER]
        }

        val obj = mock<UserNotifier>(MockMode.autofill) // create default impl of interface
        this[KEY_USER_NOTIFIER] = obj
    }

    fun HOTestCtx.`then model notify user to`(what: String) {
        val obj: UserNotifier = this[KEY_USER_NOTIFIER]
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

            else -> throw IllegalArgumentException("unknown option: $what")
        }
    }
}
