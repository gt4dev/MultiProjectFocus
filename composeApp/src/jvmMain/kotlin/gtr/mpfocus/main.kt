package gtr.mpfocus

import gtr.mpfocus.cmd_handler.CommandHandler
import gtr.mpfocus.domain.domainModule
import gtr.mpfocus.domain.model.commands.CommandParser
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.domain.model.core.CoreActions
import gtr.mpfocus.infra.infraModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = runBlocking {
    val koinApp = startKoin {
        modules(
            infraModule(),
            domainModule(),
        )
    }

    val koin = koinApp.koin
    val coreActions: CoreActions = koin.get()

    require(args.isNotEmpty()) { "Must provide command to execute" }
    val command = CommandParser.parse(args.first())

    val commandHandler = CommandHandler(coreActions)
    when (val result = commandHandler.handle(command)) {
        ActionResult.Success -> println("Command executed successfully")
        is ActionResult.Error -> println("Command failed: ${result.msg}")
    }
}

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "mpfocus",
//    ) {
//        App()
//    }
//}
