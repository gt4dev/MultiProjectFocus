package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFiles

data class ProjectConfig(
    private val fileNames: Map<ProjectFiles, String>
) {
    fun fileName(file: ProjectFiles): String {
        return fileNames[file] ?: throw IllegalStateException("option $file has no file name")
    }
}

interface ConfigService {

    suspend fun getProjectConfig(): ProjectConfig

    // temporary, basic implementation with 'hard coded' settings
    object Basic : ConfigService {
        override suspend fun getProjectConfig(): ProjectConfig {
            val filesNames = ProjectFiles.entries.associateWith { file ->
                when (file) {
                    ProjectFiles.File1 -> "main.md"
                    ProjectFiles.File2 -> "dists.md"
                    ProjectFiles.File3 -> "others.md"
                    else -> "file${file.ordinal}.md"
                }
            }
            return ProjectConfig(filesNames)
        }
    }
}
