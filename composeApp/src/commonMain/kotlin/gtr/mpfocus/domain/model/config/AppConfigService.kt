package gtr.mpfocus.domain.model.config

import gtr.mpfocus.system_actions.FolderPath
import okio.Path.Companion.toPath

expect fun getUserHomePath(): String


interface AppConfigService {
    fun getAppMainFolder(): FolderPath
}

// todo: use this service in 'repo impl', instead of hard-coded path
class AppConfigServiceImpl : AppConfigService {
    override fun getAppMainFolder(): FolderPath {
        return FolderPath((getUserHomePath() + "/.mpfocus").toPath())
    }
}
