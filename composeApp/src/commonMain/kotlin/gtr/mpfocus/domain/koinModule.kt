package gtr.mpfocus.domain

import gtr.mpfocus.domain.model.config.AppConfigService
import gtr.mpfocus.domain.model.config.GlobalProjectConfigService
import gtr.mpfocus.domain.model.config.GlobalProjectConfigServiceImpl
import gtr.mpfocus.domain.model.config.LocalProjectConfigService
import gtr.mpfocus.domain.model.config.ProjectConfigService
import gtr.mpfocus.domain.model.config.ProjectConfigServiceImpl
import gtr.mpfocus.domain.model.config.project_config.GlobalTomlFileActions
import gtr.mpfocus.domain.model.config.project_config.GlobalTomlParser
import gtr.mpfocus.domain.model.core.CreateProjectService
import gtr.mpfocus.domain.model.core.CreateProjectServiceImpl
import gtr.mpfocus.domain.model.core.ProjectActions
import gtr.mpfocus.domain.model.core.ProjectActionsImpl
import gtr.mpfocus.domain.model.init_data.DataLoader
import gtr.mpfocus.domain.model.read.ProjectReadModel
import gtr.mpfocus.domain.model.read.ProjectReadModelImpl
import org.koin.dsl.module

fun domainModule() = module {
    single {
        GlobalTomlFileActions(
            get(),
            get<AppConfigService>().getAppMainFolder()
        )
    }
    single { GlobalTomlParser() }
    single<GlobalProjectConfigService> { GlobalProjectConfigServiceImpl(get(), get()) }
    single<LocalProjectConfigService> { LocalProjectConfigService.NullConfig }
    single<ProjectConfigService> { ProjectConfigServiceImpl(get(), get()) }
    single<CreateProjectService> { CreateProjectServiceImpl(get(), get()) }
    single<ProjectActions> { ProjectActionsImpl(get(), get(), get(), get()) }
    single<ProjectReadModel> { ProjectReadModelImpl(get(), get()) }
    single { DataLoader(get(), get()) }
}
