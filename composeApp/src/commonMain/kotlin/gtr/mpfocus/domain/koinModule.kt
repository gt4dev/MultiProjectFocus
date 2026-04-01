package gtr.mpfocus.domain

import gtr.mpfocus.domain.model.config.ProjectConfigReader
import gtr.mpfocus.domain.model.core.CreateProjectService
import gtr.mpfocus.domain.model.core.CreateProjectServiceImpl
import gtr.mpfocus.domain.model.core.ProjectActions
import gtr.mpfocus.domain.model.core.ProjectActionsImpl
import gtr.mpfocus.domain.model.init_data.DataLoader
import org.koin.dsl.module

fun domainModule() = module {
    single<ProjectConfigReader> { ProjectConfigReader.HardCodedConfig }
    single<CreateProjectService> { CreateProjectServiceImpl(get(), get()) }
    single<ProjectActions> { ProjectActionsImpl(get(), get(), get(), get()) }
    single { DataLoader(get(), get()) }
}
