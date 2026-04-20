package gtr.mpfocus.ui

import gtr.mpfocus.ui.core.AppUi
import gtr.mpfocus.ui.core.AppUiImpl
import gtr.mpfocus.ui.core.AppWindowLauncher
import gtr.mpfocus.ui.core.createAppWindowLauncher
import gtr.mpfocus.ui.create_file_dialog.CreateFileDialogViewModelFactoryProvider
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogViewModelFactoryProvider
import gtr.mpfocus.ui.create_project_dialog.createFolderPicker
import gtr.mpfocus.ui.main_screen.MainScreenViewModelFactoryProvider
import gtr.mpfocus.ui.main_screen.MainScreenWindowFactory
import org.koin.dsl.lazyModule

fun uiModule() = lazyModule {
    single<AppWindowLauncher> { createAppWindowLauncher() }
    single { MainScreenViewModelFactoryProvider(get(), get(), get()) }
    single { MainScreenWindowFactory(get(), get(), get()) }
    single { CreateProjectDialogViewModelFactoryProvider(get(), get(), get()) }
    single { CreateFileDialogViewModelFactoryProvider(get(), get()) }
    single { createFolderPicker() }
    single<AppUi> { AppUiImpl(get(), get()) }
}
