package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath

data class ProjectConfig(
    private val fileNames: Map<ProjectFile, String>
) {
    fun fileName(file: ProjectFile): String {
        return fileNames[file] ?: throw IllegalStateException("option $file has no file name")
    }
}

interface ProjectConfigService {

    suspend fun getProjectConfig(projectPath: FolderPath): ProjectConfig
}
