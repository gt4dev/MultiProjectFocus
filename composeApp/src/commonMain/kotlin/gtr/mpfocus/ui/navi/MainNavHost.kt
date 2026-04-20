package gtr.mpfocus.ui.navi

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.create_file_dialog.CreateFileDialog
import gtr.mpfocus.ui.create_file_dialog.CreateFileDialogContainer
import gtr.mpfocus.ui.create_file_dialog.CreateFileDialogViewModelFactoryProvider
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogContainer
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogViewModelFactoryProvider
import gtr.mpfocus.ui.main_screen.MainScreenContainer
import gtr.mpfocus.ui.main_screen.MainScreenViewModelFactoryProvider
import kotlinx.serialization.Serializable
import okio.Path.Companion.toPath

object Routes {

    @Serializable
    data object MainScreen

    @Serializable
    data class CreateProjectDialog(
        val relatedProjectId: Long? = null,
    )

    @Serializable
    data class CreateFileDialog(
        val comment: String? = null,
        val fileName: String,
        val folderPath: String,
    )
}


@Composable
fun MainNavHost(
    mainScreenViewModelFactoryProvider: MainScreenViewModelFactoryProvider,
    initialMessage: MessagePanelState? = null,
    createProjectDialogViewModelFactoryProvider: CreateProjectDialogViewModelFactoryProvider,
    createFileDialogViewModelFactoryProvider: CreateFileDialogViewModelFactoryProvider,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MainScreen,
    ) {
        composable<Routes.MainScreen> {
            MainScreenContainer(
                viewModelFactory = mainScreenViewModelFactoryProvider.createFactory(
                    initialMessage = initialMessage,
                ),
                onCreateProjectDialogOpen = { relatedProjectId ->
                    navController.navigate(Routes.CreateProjectDialog(relatedProjectId))
                },
                onCreateFileDialogOpen = { startParameters ->
                    navController.navigate(
                        Routes.CreateFileDialog(
                            comment = startParameters.extraInfo,
                            fileName = startParameters.file.fileName,
                            folderPath = startParameters.file.folderPath.path.toString(),
                        )
                    )
                },
            )
        }

        dialog<Routes.CreateProjectDialog> { backStackEntry ->
            val relatedProjectId = backStackEntry
                .toRoute<Routes.CreateProjectDialog>()
                .relatedProjectId
            CreateProjectDialogContainer(
                viewModelFactory = createProjectDialogViewModelFactoryProvider.createFactory(relatedProjectId),
                onCloseRequest = {
                    navController.popBackStack()
                },
                onCompleted = {
                    navController.popBackStack()
                },
            )
        }

        dialog<Routes.CreateFileDialog> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.CreateFileDialog>()
            val startParameters = CreateFileDialog.StartParameters(
                extraInfo = route.comment,
                file = FilePath(
                    fileName = route.fileName,
                    folderPath = FolderPath(route.folderPath.toPath()),
                ),
            )

            CreateFileDialogContainer(
                viewModelFactory = createFileDialogViewModelFactoryProvider.createFactory(startParameters),
                onCloseRequest = {
                    navController.popBackStack()
                },
                onCompleted = {
                    navController.popBackStack()
                },
            )
        }
    }
}
