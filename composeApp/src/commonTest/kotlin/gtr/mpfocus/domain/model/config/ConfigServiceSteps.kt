package gtr.mpfocus.domain.model.config

import dev.hotest.HOTestCtx
import dev.mokkery.answering.calls
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.delay
import okio.Path.Companion.toPath
import kotlin.time.Duration


object ConfigServiceSteps {

    fun HOTestCtx.`given 'basic config service' exists`() {
        koinAdd {
            single<GlobalProjectConfigService> { GlobalProjectConfigService.NullConfig }
            single<LocalProjectConfigService> { LocalProjectConfigService.NullConfig }
            single<ProjectConfigService> { ProjectConfigServiceImpl(get(), get()) }
        }
    }

    fun HOTestCtx.`given 'project config service mock' returns project config as follows`(
        responseDelay: Duration,
        projectConfigs: List<Models.ProjectConfig>,
    ) {
        fun toProjectConfig(fileNames: List<String>): ProjectConfig {
            require(fileNames.size == ProjectFile.entries.size) {
                "Expected ${ProjectFile.entries.size} file names but got ${fileNames.size}"
            }
            val fileNamesMap = buildMap {
                ProjectFile.entries.forEachIndexed { index, fileId ->
                    put(fileId, fileNames[index])
                }
            }
            return ProjectConfig(fileNamesMap)
        }

        val configMap = projectConfigs.associate { config ->
            FolderPath(config.requestedProjectPath.toPath()) to toProjectConfig(config.returnedProjFiles)
        }
        val service = mock<ProjectConfigService>()
        everySuspend { service.getProjectConfig(any()) } calls { (projectPath: FolderPath) ->
            delay(responseDelay)
            configMap[projectPath] ?: throw IllegalStateException("No config for $projectPath")
        }
        koinAddObject<ProjectConfigService>(service)
    }

}

