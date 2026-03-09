package gtr.mpfocus.ui

import gtr.mpfocus.ui.core.AppUi
import gtr.mpfocus.ui.core.AppUiImpl
import gtr.mpfocus.ui.core.AppWindowLauncher
import gtr.mpfocus.ui.core.createAppWindowLauncher
import gtr.mpfocus.ui.main_screen.MainScreenViewModelFactory
import gtr.mpfocus.ui.main_screen.MainScreenWindowFactory
import org.koin.dsl.lazyModule

fun uiModule() = lazyModule {
    single<AppWindowLauncher> { createAppWindowLauncher() }
    single { MainScreenViewModelFactory(get(), get()) }
    single { MainScreenWindowFactory(get()) }
    single<AppUi> { AppUiImpl(get(), get()) }
}
