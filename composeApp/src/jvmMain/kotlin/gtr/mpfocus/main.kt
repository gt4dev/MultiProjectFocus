package gtr.mpfocus

import gtr.mpfocus.cmd_handler.CommandHandler
import gtr.mpfocus.domain.domainModule
import gtr.mpfocus.domain.model.commands.CommandParser
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.infra.infraModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = runBlocking {

    require(args.isNotEmpty()) { "Provide command to execute" }
    val command = CommandParser.parse(args.first())

    val koinApp = startKoin {
        modules(
            infraModule(),
            domainModule(),
            appModule()
        )
    }
    val koin = koinApp.koin

    val commandHandler: CommandHandler = koin.get()
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
