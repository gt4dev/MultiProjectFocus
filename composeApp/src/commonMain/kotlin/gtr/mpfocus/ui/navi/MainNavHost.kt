package gtr.mpfocus.ui.navi

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogContainer
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogViewModelFactory
import gtr.mpfocus.ui.main_screen.MainScreenContainer
import kotlinx.serialization.Serializable

object Routes {

    @Serializable
    data object MainScreen

    @Serializable
    data class CreateProjectDialog(
        val relatedProjectId: Long? = null,
    )
}


@Composable
fun MainNavHost(
    mainScreenViewModelFactory: ViewModelProvider.Factory,
    createProjectDialogViewModelFactory: CreateProjectDialogViewModelFactory,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MainScreen,
    ) {
        composable<Routes.MainScreen> {
            MainScreenContainer(
                viewModelFactory = mainScreenViewModelFactory,
                onCreateProjectDialogOpen = { relatedProjectId ->
                    navController.navigate(Routes.CreateProjectDialog(relatedProjectId))
                },
            )
        }

        dialog<Routes.CreateProjectDialog> { backStackEntry ->
            val relatedProjectId = backStackEntry
                .toRoute<Routes.CreateProjectDialog>()
                .relatedProjectId
            CreateProjectDialogContainer(
                viewModelFactory = createProjectDialogViewModelFactory.create(relatedProjectId),
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
