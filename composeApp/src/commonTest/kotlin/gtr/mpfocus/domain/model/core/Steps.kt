package gtr.mpfocus.domain.model.core

import gtr.hotest.HOTestCtx

object Steps {

    private const val KEY_ACTION_PREFERENCES = "KEY_ACTION_PREFERENCES"

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

}