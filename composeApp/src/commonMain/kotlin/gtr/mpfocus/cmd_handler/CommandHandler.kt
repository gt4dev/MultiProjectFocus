package gtr.mpfocus.cmd_handler

import gtr.mpfocus.domain.model.commands.*
import gtr.mpfocus.domain.model.core.ActionPreferences
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.domain.model.core.CoreActions
import gtr.mpfocus.domain.model.core.UserNotifier

class CommandHandler(
    private val coreActions: CoreActions,
    private val actionPreferences: ActionPreferences = ActionPreferences(),
    private val userNotifier: UserNotifier = UserNotifier.None,
) {
    suspend fun handle(command: Command): ActionResult {
        return when (command) {
            ProjectCurrent.OpenFolder -> coreActions.openCurrentProjectFolder(
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectCurrent.OpenFile -> coreActions.openCurrentProjectFile(
                file = command.file,
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectPinned.OpenFolder -> coreActions.openPinnedProjectFolder(
                pinPosition = command.pinPosition,
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectPinned.OpenFile -> coreActions.openPinnedProjectFile(
                pinPosition = command.pinPosition,
                file = command.file,
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectByPath.OpenFile -> ActionResult.Error(
                "ProjectByPath.OpenFile is not supported by CoreActions yet"
            )

            is LoadInitialData -> ActionResult.Error(
                "LoadInitialData is not supported by CoreActions yet"
            )
        }
    }
}
