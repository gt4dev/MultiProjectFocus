package gtr.mpfocus.ui.navi

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogContainer
import gtr.mpfocus.ui.main_screen.MainScreenContainer
import kotlinx.serialization.Serializable

object Routes {

    @Serializable
    data object MainScreen

    @Serializable
    data object CreateProjectDialog
}


@Composable
fun MainNavHost(
    mainScreenViewModelFactory: ViewModelProvider.Factory,
    createProjectDialogViewModelFactory: ViewModelProvider.Factory,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MainScreen,
    ) {
        composable<Routes.MainScreen> {
            MainScreenContainer(
                viewModelFactory = mainScreenViewModelFactory,
                onCreateProjectDialogOpen = {
                    navController.navigate(Routes.CreateProjectDialog)
                },
            )
        }

        dialog<Routes.CreateProjectDialog> {
            CreateProjectDialogContainer(
                viewModelFactory = createProjectDialogViewModelFactory,
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
