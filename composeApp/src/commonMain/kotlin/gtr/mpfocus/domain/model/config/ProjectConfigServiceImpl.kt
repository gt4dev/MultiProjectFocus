package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath

class ProjectConfigServiceImpl(
    private val projectGlobalConfigService: ProjectGlobalConfigService,
    private val projectLocalConfigService: ProjectLocalConfigService,
) : ProjectConfigService {

    override suspend fun getProjectConfig(projectPath: FolderPath): ProjectConfig {
        val local = projectLocalConfigService.readConfig(projectPath)
        val global = projectGlobalConfigService.readConfig()
        val default = getDefaultProjectConfig()
        return merge(local, global, default)
    }

    private fun merge(
        local: ProjectLocalConfig?,
        global: ProjectGlobalConfig?,
        default: ProjectConfig,
    ): ProjectConfig {
        val fileNames = ProjectFile.entries.associateWith { file ->
            local?.fileNames?.get(file)
                ?: global?.fileNames?.get(file)
                ?: default.fileName(file)
        }
        return ProjectConfig(fileNames)
    }

    // todo: move "custom file names" config to 'global'
    private fun getDefaultProjectConfig(): ProjectConfig {
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
