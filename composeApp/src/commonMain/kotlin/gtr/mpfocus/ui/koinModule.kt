package gtr.mpfocus.ui

import gtr.mpfocus.ui.core.AppUi
import gtr.mpfocus.ui.core.AppUiImpl
import org.koin.dsl.lazyModule

fun uiModule() = lazyModule {
    single<AppUi> { AppUiImpl() }
}
