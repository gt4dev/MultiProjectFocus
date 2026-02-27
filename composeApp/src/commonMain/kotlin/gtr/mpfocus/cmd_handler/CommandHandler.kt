package gtr.mpfocus.cmd_handler

import gtr.mpfocus.domain.model.commands.*
import gtr.mpfocus.domain.model.core.ActionPreferences
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.domain.model.core.CoreActions
import gtr.mpfocus.domain.model.core.UserNotifier
import gtr.mpfocus.domain.model.init_data.DataLoader
import gtr.mpfocus.ui.core.AppUi
import gtr.mpfocus.ui.core.TextMessage

/**
 * Lazy loading - notes:
 * - commands require part of objects from CommandHandler constructor
 * - thus - to shorten the app start time - all objects are created `lazy`
 */
class CommandHandler(
    private val coreActions: Lazy<CoreActions>,
    private val dataLoader: Lazy<DataLoader>,
    private val appUi: Lazy<AppUi>,
    private val actionPreferences: ActionPreferences = ActionPreferences(),
    private val userNotifier: UserNotifier = UserNotifier.None,
) {
    suspend fun handle(command: Command): ActionResult {
        return when (command) {
            NoExplicitCommand -> {
                appUi.value.showMessage(
                    TextMessage(
                        """
                        You started MPF without parameters.
                        MPF works best if started: 
                        1/ with parameters 
                        2/ from Key Manager (like AutoHotKey) 
                        
                        See guide for details.
                        """.trimIndent()
                    )
                )

                ActionResult.Success
            }

            ProjectCurrent.OpenFolder -> coreActions.value.openCurrentProjectFolder(
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectCurrent.OpenFile -> coreActions.value.openCurrentProjectFile(
                file = command.file,
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectPinned.OpenFolder -> coreActions.value.openPinnedProjectFolder(
                pinPosition = command.pinPosition,
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectPinned.OpenFile -> coreActions.value.openPinnedProjectFile(
                pinPosition = command.pinPosition,
                file = command.file,
                actionPreferences = actionPreferences,
                userNotifier = userNotifier
            )

            is ProjectByPath.OpenFile -> ActionResult.Error(
                "ProjectByPath.OpenFile is not supported by CoreActions yet"
            )

            is LoadInitialData -> {
                dataLoader.value.loadData(command.tomlFilePath)
                appUi.value.showMessage(
                    TextMessage(
                        """
                        Data from TOML file loaded.
                        """.trimIndent()
                    )
                )
                ActionResult.Success
            }
        }
    }
}
