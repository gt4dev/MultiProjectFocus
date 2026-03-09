package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile

data class ProjectConfig(
    private val fileNames: Map<ProjectFile, String>
) {
    fun fileName(file: ProjectFile): String {
        return fileNames[file] ?: throw IllegalStateException("option $file has no file name")
    }
}

interface ConfigService {

    suspend fun getProjectConfig(): ProjectConfig

    // temporary, basic implementation with 'hard coded' settings
    object Basic : ConfigService {
        override suspend fun getProjectConfig(): ProjectConfig {
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
    }
}
