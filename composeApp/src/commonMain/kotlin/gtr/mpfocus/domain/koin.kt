package gtr.mpfocus.domain

import gtr.mpfocus.domain.model.config.ConfigService
import gtr.mpfocus.domain.model.core.CoreActions
import gtr.mpfocus.domain.model.core.CoreActionsImpl
import gtr.mpfocus.domain.repository.DevTimeProjectRepositoryImpl
import gtr.mpfocus.domain.repository.ProjectRepository
import org.koin.dsl.module

fun domainModule() = module {
    single<ProjectRepository> { DevTimeProjectRepositoryImpl(true) }
    single<ConfigService> { ConfigService.Basic }

    single { CoreActionsImpl(get(), get(), get(), get(), get()) }
    single<CoreActions> { get<CoreActionsImpl>() }
}
