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

interface ProjectConfigReader {

    suspend fun getGlobalProjectConfig(): ProjectConfig

    suspend fun getLocalProjectConfig(projectPath: FolderPath): ProjectConfig

    // temporary, basic implementation with 'hard coded' settings
    object HardCodedConfig : ProjectConfigReader {
        override suspend fun getGlobalProjectConfig(): ProjectConfig {
            val filesNames = ProjectFile.entries.associateWith { file ->
                when (file) {
                    ProjectFile.File1 -> "main.md"
                    ProjectFile.File2 -> "dists.md"
                    ProjectFile.File3 -> "others.md"
                    else -> "file${file.ordinal}.md"
                }
            }
            return ProjectConfig(filesNames)
        }

        override suspend fun getLocalProjectConfig(projectPath: FolderPath): ProjectConfig {
            return getGlobalProjectConfig()
        }
    }
}
