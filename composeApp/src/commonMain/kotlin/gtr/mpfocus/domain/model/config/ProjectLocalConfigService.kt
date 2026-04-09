package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath

data class ProjectLocalConfig(
    val projectName: String? = null,
    val fileNames: Map<ProjectFile, String> = emptyMap()
)

interface ProjectLocalConfigService {
    suspend fun readConfig(folder: FolderPath): ProjectLocalConfig?

    object NullConfig : ProjectLocalConfigService {
        override suspend fun readConfig(folder: FolderPath): ProjectLocalConfig? = null
    }
}
