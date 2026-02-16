package gtr.mpfocus

import gtr.mpfocus.domain.model.config.ConfigService
import gtr.mpfocus.domain.model.core.ActionPreferences
import gtr.mpfocus.domain.model.core.CoreActionsImpl
import gtr.mpfocus.domain.model.core.UserNotifier
import gtr.mpfocus.domain.model.repos.DevTimeProjectsRepoImpl
import gtr.mpfocus.system_actions.FileSystemActionsImpl
import gtr.mpfocus.system_actions.OperatingSystemActionsImpl
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val operatingSystemActions = OperatingSystemActionsImpl()
    val fileSystemActions = FileSystemActionsImpl()
    val projectsRepo = DevTimeProjectsRepoImpl(true)
    val configService = ConfigService.Basic

    val m = CoreActionsImpl(
        operatingSystemActions,
        fileSystemActions,
        projectsRepo,
        configService,
    )
    val result = m.openCurrentProjectFolder(
        ActionPreferences(),
        UserNotifier.None
    )
    println(result)
}

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "mpfocus",
//    ) {
//        App()
//    }
//}
