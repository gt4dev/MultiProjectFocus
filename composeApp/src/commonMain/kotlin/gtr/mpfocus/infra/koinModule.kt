package gtr.mpfocus.infra

import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.infra.db_repo.MPFDatabase
import gtr.mpfocus.infra.db_repo.ProjectDao
import gtr.mpfocus.infra.db_repo.ProjectRepositoryRoomImpl
import gtr.mpfocus.infra.db_repo.createMPFDatabase
import org.koin.dsl.module

fun infraModule() = module {
    single<MPFDatabase> { createMPFDatabase() }

    single<ProjectDao> { get<MPFDatabase>().projectDao() }
    single<ProjectRepository> { ProjectRepositoryRoomImpl(get()) }
}
