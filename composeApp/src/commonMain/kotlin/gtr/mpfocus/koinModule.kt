package gtr.mpfocus

import gtr.mpfocus.cmd_handler.CommandHandler
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FileSystemActionsImpl
import gtr.mpfocus.system_actions.OperatingSystemActions
import gtr.mpfocus.system_actions.OperatingSystemActionsImpl
import org.koin.dsl.module

fun appModule() = module {
    single<OperatingSystemActions> { OperatingSystemActionsImpl() }
    single<FileSystemActions> { FileSystemActionsImpl() }
    single<CommandHandler> { CommandHandler(lazy { get() }, lazy { get() }) }
}
