package gtr.mpfocus.domain

import gtr.mpfocus.domain.model.config.ConfigService
import gtr.mpfocus.domain.model.core.*
import gtr.mpfocus.domain.model.init_data.DataLoader
import org.koin.dsl.module

fun domainModule() = module {
    single<ConfigService> { ConfigService.Basic }
    single { CreateProjectServiceImpl(get(), get()) }
    single<CreateProjectService> { get<CreateProjectServiceImpl>() }
    single { CoreActionsImpl(get(), get(), get(), get()) }
    single<CoreActions> { get<CoreActionsImpl>() }
    single { ProjectActionsImpl(get(), get(), get(), get()) }
    single<ProjectActions> { get<ProjectActionsImpl>() }
    single { DataLoader(get(), get()) }
}
