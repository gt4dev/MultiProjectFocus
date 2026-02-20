package gtr.mpfocus.domain

import gtr.mpfocus.domain.model.config.ConfigService
import gtr.mpfocus.domain.model.core.CoreActions
import gtr.mpfocus.domain.model.core.CoreActionsImpl
import gtr.mpfocus.domain.model.init_data.DataLoader
import org.koin.dsl.module

fun domainModule() = module {
    single<ConfigService> { ConfigService.Basic }
    single { CoreActionsImpl(get(), get(), get(), get()) }
    single<CoreActions> { get<CoreActionsImpl>() }
    single { DataLoader(get(), get()) }
}
