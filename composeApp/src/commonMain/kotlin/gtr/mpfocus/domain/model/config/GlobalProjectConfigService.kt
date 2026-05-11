package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile

data class GlobalProjectConfig(
    val fileNames: Map<ProjectFile, String> = emptyMap()
)

interface GlobalProjectConfigService {
    suspend fun readConfig(): GlobalProjectConfig?

    suspend fun openConfigFile()

    object NullConfig : GlobalProjectConfigService {
        override suspend fun readConfig(): GlobalProjectConfig? = null

        override suspend fun openConfigFile() = Unit
    }
}
