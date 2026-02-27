package gtr.mpfocus

import gtr.mpfocus.cmd_handler.CommandHandler
import gtr.mpfocus.domain.domainModule
import gtr.mpfocus.domain.model.commands.CommandParser
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.infra.infraModule
import gtr.mpfocus.ui.uiModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

fun main(args: Array<String>): Unit = runBlocking {

    val command = CommandParser.parseArgs(args)

    val koinApp = startKoin {
        modules(
            infraModule(),
            domainModule(),
            appModule()
        )
        lazyModules(
            uiModule()
        )
    }
    val koin = koinApp.koin

    val commandHandler: CommandHandler = koin.get()
    when (val result = commandHandler.handle(command)) {
        ActionResult.Success -> println("Command executed successfully")
        is ActionResult.Error -> println("Command failed: ${result.msg}")
    }

//    application {
//        Window(
//            onCloseRequest = ::exitApplication,
//            title = "mpfocus",
//        ) {
//            App()
//        }
//    }
}
