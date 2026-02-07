package gtr.mpfocus.domain.model.core

import gtr.hotest.HOTestCtx
import kotlin.test.assertEquals

object Steps {

    private const val KEY_ACTION_PREFERENCES = "KEY_ACTION_PREFERENCES"
    private const val KEY_CORE_ACTIONS = "KEY_CORE_ACTIONS"
    private const val KEY_CORE_ACTIONS_RESULT = "KEY_CORE_ACTIONS_RESULT"

    fun HOTestCtx.`given action preference 'if no folder' is`(pref: String) {

        val ifNoFolder: ActionPreferences.IfNoFileOrFolder = when (pref) {
            "auto create" -> ActionPreferences.IfNoFileOrFolder.AutoCreate
            "report error" -> ActionPreferences.IfNoFileOrFolder.ReportError
            "ask user" -> ActionPreferences.IfNoFileOrFolder.AskUser
            else -> throw IllegalArgumentException("Unknown preference $pref")
        }

        this[KEY_ACTION_PREFERENCES] = ActionPreferences(
            ifNoFolder = ifNoFolder
        )
    }

    fun HOTestCtx.`given exists 'real model' service`() {
        this[KEY_CORE_ACTIONS] = CoreActionsImpl(
            this[gtr.mpfocus.system_actions.Steps.KEY_OPERATING_SYSTEM_ACTIONS],
            this[gtr.mpfocus.system_actions.Steps.KEY_FILE_SYSTEM_ACTIONS],
        )
    }

    suspend fun HOTestCtx.`when model executes command 'open folder in current project'`() {
        val coreActions: CoreActions = this[KEY_CORE_ACTIONS]
        val aps: ActionPreferences = this[KEY_ACTION_PREFERENCES]
        val result = coreActions.openCurrentProjectFolder(aps)
        this[KEY_CORE_ACTIONS_RESULT] = result
    }

    fun HOTestCtx.`then model returns success`() {
        assertEquals(ActionResult.Success, this[KEY_CORE_ACTIONS_RESULT])
    }
}