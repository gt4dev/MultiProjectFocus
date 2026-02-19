package gtr.mpfocus.infra

import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.infra.db_repo.MPFDatabase
import gtr.mpfocus.infra.db_repo.ProjectDao
import gtr.mpfocus.infra.db_repo.ProjectRepositoryRoomImpl
import gtr.mpfocus.infra.db_repo.createMPFDatabase
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FileSystemActionsImpl
import gtr.mpfocus.system_actions.OperatingSystemActions
import gtr.mpfocus.system_actions.OperatingSystemActionsImpl
import org.koin.dsl.module

fun infraModule() = module {
    single<MPFDatabase> { createMPFDatabase() }

    single<ProjectDao> { get<MPFDatabase>().projectDao() }
    single<ProjectRepository> { ProjectRepositoryRoomImpl(get()) }

    single<OperatingSystemActions> { OperatingSystemActionsImpl() }
    single<FileSystemActions> { FileSystemActionsImpl() }
}
