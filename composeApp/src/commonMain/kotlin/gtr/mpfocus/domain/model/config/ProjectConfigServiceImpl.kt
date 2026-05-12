package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ProjectConfigServiceImpl(
    private val globalProjectConfigService: GlobalProjectConfigService,
    private val localProjectConfigService: LocalProjectConfigService,
) : ProjectConfigService {

    private val _configChanges = MutableSharedFlow<Unit>()
    override val configChanges: Flow<Unit> = _configChanges.asSharedFlow()

    override suspend fun getProjectConfig(projectPath: FolderPath): ProjectConfig {
        val local = localProjectConfigService.readConfig(projectPath)
        val global = globalProjectConfigService.readConfig()
        val default = getDefaultProjectConfig()
        return merge(local, global, default)
    }

    override suspend fun reloadConfigs() {
        globalProjectConfigService.invalidateCache()
        localProjectConfigService.invalidateCache()
        _configChanges.emit(Unit)
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
