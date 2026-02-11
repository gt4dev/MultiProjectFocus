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
                    ProjectFiles.File0 -> "main.md"
                    ProjectFiles.File1 -> "dists.md"
                    ProjectFiles.File2 -> "plan.md"
                    else -> "file${file.ordinal}.md"
                }
            }
            return ProjectConfig(filesNames)
        }
    }
}
