package gtr.mpfocus

import gtr.mpfocus.cmd_handler.CommandHandler
import gtr.mpfocus.domain.domainModule
import gtr.mpfocus.domain.model.commands.CommandParser
import gtr.mpfocus.infra.infraModule
import gtr.mpfocus.ui.uiModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

fun main(args: Array<String>): Unit = runBlocking {
    fixSkiko()

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
    commandHandler.handle(command)
}
