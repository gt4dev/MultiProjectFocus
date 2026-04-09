package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile

data class ProjectGlobalConfig(
    val fileNames: Map<ProjectFile, String> = emptyMap()
)

interface ProjectGlobalConfigService {
    suspend fun readConfig(): ProjectGlobalConfig?

    object NullConfig : ProjectGlobalConfigService {
        override suspend fun readConfig(): ProjectGlobalConfig? = null
    }
}
