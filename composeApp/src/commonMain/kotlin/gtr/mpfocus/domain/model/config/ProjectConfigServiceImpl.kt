package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath

class ProjectConfigServiceImpl(
    private val globalProjectConfigService: GlobalProjectConfigService,
    private val localProjectConfigService: LocalProjectConfigService,
) : ProjectConfigService {

    override suspend fun getProjectConfig(projectPath: FolderPath): ProjectConfig {
        val local = localProjectConfigService.readConfig(projectPath)
        val global = globalProjectConfigService.readConfig()
        val default = getDefaultProjectConfig()
        return merge(local, global, default)
    }

    private fun merge(
        local: LocalProjectConfig?,
        global: GlobalProjectConfig?,
        default: ProjectConfig,
    ): ProjectConfig {
        val fileNames = ProjectFile.entries.associateWith { file ->
            local?.fileNames?.get(file)
                ?: global?.fileNames?.get(file)
                ?: default.fileName(file)
        }
        return ProjectConfig(fileNames)
    }

    private fun getDefaultProjectConfig(): ProjectConfig {
        val filesNames = ProjectFile.entries.associateWith { file ->
            "file${file.ordinal}.md"
        }
        return ProjectConfig(filesNames)
    }
}
