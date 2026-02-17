package gtr.mpfocus.infra

import gtr.mpfocus.domain.repository.PersonRepository
import gtr.mpfocus.infra.db_repo.MPFDatabase
import gtr.mpfocus.infra.db_repo.PersonRepositoryRoomImpl
import gtr.mpfocus.infra.db_repo.createMPFDatabase
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FileSystemActionsImpl
import gtr.mpfocus.system_actions.OperatingSystemActions
import gtr.mpfocus.system_actions.OperatingSystemActionsImpl
import org.koin.dsl.module

fun infraModule() = module {
    single { createMPFDatabase() }
    single { get<MPFDatabase>().personDao() }
    single<PersonRepository> { PersonRepositoryRoomImpl(get()) }

    single<OperatingSystemActions> { OperatingSystemActionsImpl() }
    single<FileSystemActions> { FileSystemActionsImpl() }
}
