package gtr.mpfocus

import gtr.mpfocus.domain.model.core.ActionPreferences
import gtr.mpfocus.domain.model.core.CoreActionsImpl
import gtr.mpfocus.domain.model.repos.DevTimeProjectsRepoImpl
import gtr.mpfocus.system_actions.FileSystemActionsImpl
import gtr.mpfocus.system_actions.OperatingSystemActionsImpl
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val operatingSystemActions = OperatingSystemActionsImpl()
    val fileSystemActions = FileSystemActionsImpl()
    val projectsRepo = DevTimeProjectsRepoImpl(true)

    val m = CoreActionsImpl(
        operatingSystemActions,
        fileSystemActions,
        projectsRepo,
    )
    val result = m.openCurrentProjectFolder(
        ActionPreferences(
            ifNoFolder = ActionPreferences.IfNoFileOrFolder.ReportError,
            ifNoFile = ActionPreferences.IfNoFileOrFolder.ReportError,
        )
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