package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath

data class LocalProjectConfig(
    val projectName: String? = null,
    val projectDescription: String? = null,
    val fileNames: Map<ProjectFile, String> = emptyMap()
)

interface LocalProjectConfigService {
    suspend fun readConfig(folder: FolderPath): LocalProjectConfig?

    suspend fun openConfigFile(projectId: Long)

    object NullConfig : LocalProjectConfigService {
        override suspend fun readConfig(folder: FolderPath): LocalProjectConfig? = null

        override suspend fun openConfigFile(projectId: Long) = Unit
    }
}
