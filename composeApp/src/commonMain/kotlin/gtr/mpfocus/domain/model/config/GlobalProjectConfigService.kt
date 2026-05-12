package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile

data class GlobalProjectConfig(
    val fileNames: Map<ProjectFile, String> = emptyMap()
)

interface GlobalProjectConfigService {
    suspend fun readConfig(): GlobalProjectConfig?

    fun invalidateCache()

    suspend fun openConfigFile()
}
