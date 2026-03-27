package gtr.mpfocus.cmd_handler

import gtr.mpfocus.domain.model.commands.Command
import gtr.mpfocus.domain.model.commands.LoadInitialData
import gtr.mpfocus.domain.model.commands.NoExplicitCommand
import gtr.mpfocus.domain.model.commands.ProjectByPath
import gtr.mpfocus.domain.model.commands.ProjectCurrent
import gtr.mpfocus.domain.model.commands.ProjectPinned
import gtr.mpfocus.domain.model.commands.ShowUi
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.domain.model.core.ProjectActions
import gtr.mpfocus.domain.model.init_data.DataLoader
import gtr.mpfocus.ui.core.AppUi
import gtr.mpfocus.ui.core.TextMessage

/**
 * Lazy loading - notes:
 * - commands require part of objects from CommandHandler constructor
 * - thus - to shorten the app start time - all objects are created `lazy`
 */
class CommandHandler(
    private val projectActions: Lazy<ProjectActions>,
    private val dataLoader: Lazy<DataLoader>,
    private val appUi: Lazy<AppUi>,
    private val projectActionPreferences: ProjectActions.Preferences = ProjectActions.Preferences.CLI,
    private val projectActionCallerNotification: ProjectActions.CallerNotification = ProjectActions.CallerNotification.CancelAll,
) {
    suspend fun handle(command: Command): ActionResult {
        return when (command) {
            ShowUi -> {
                appUi.value.showMainWindow()
                ActionResult.Success
            }

            NoExplicitCommand -> {
                appUi.value.showMessage(
                    TextMessage(
                        """
                        You started Multi Project Focus (MPF) app without any parameters.
                        That’s fine, but MPF works best when running in the background and opening your projects via keyboard shortcuts.
                        Read the guide to learn how.
                        """.trimIndent()
                    )
                )
                ActionResult.Success
            }

            ProjectCurrent.OpenFolder -> projectActions.value.openCurrentProjectFolder(
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification
            )

            is ProjectCurrent.OpenFile -> projectActions.value.openCurrentProjectFile(
                file = command.file,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification
            )

            is ProjectPinned.OpenFolder -> projectActions.value.openPinnedProjectFolder(
                pinPosition = command.pinPosition,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification
            )

            is ProjectPinned.OpenFile -> projectActions.value.openPinnedProjectFile(
                pinPosition = command.pinPosition,
                file = command.file,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification
            )

            is ProjectByPath.OpenFile -> ActionResult.Error(
                "ProjectByPath.OpenFile is not supported by ProjectActions yet"
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
