package gtr.mpfocus.domain.model.config

import dev.hotest.HOTestCtx
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.hotest.koinAddIfMissing
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import okio.Path.Companion.toPath
import kotlin.time.Duration


object ConfigServiceSteps {

    fun HOTestCtx.`given 'basic config service' exists`() {
        koinAdd {
            single<GlobalProjectConfigService> { NullReturningGlobalProjectConfig }
            single<LocalProjectConfigService> { NullReturningLocalProjectConfig }
            single<ProjectConfigService> { ProjectConfigServiceImpl(get(), get()) }
        }
    }

    fun HOTestCtx.`given 'project config service mock' returns project configs`(
        responseDelay: Duration,
        projectConfigs: List<Models.ProjectConfig>,
    ) {
        val state = initProjectConfigServiceMock()
        state.responseDelay = responseDelay
        state.configMap = toConfigMap(projectConfigs)
    }

    fun HOTestCtx.`given 'project config service mock' has observable 'config changes'`() {
        val state = initProjectConfigServiceMock()
        val service = koin.get<ProjectConfigService>()

        state.configChanges = MutableSharedFlow()
        every { service.configChanges } calls {
            state.configChanges ?: emptyFlow()
        }
    }

    fun HOTestCtx.`given 'project config service mock' can mimic config reloads`() {
        val state = initProjectConfigServiceMock()
        val service = koin.get<ProjectConfigService>()

        everySuspend { service.reloadConfigs() } calls {
            state.configChanges?.emit(Unit)
        }
    }

    suspend fun HOTestCtx.`when 'project config service' is asked to reload configs`() {
        koin.get<ProjectConfigService>().reloadConfigs()
    }

    private fun HOTestCtx.initProjectConfigServiceMock(): ProjectConfigServiceMockState {
        val state = koinAddIfMissing {
            ProjectConfigServiceMockState()
        }
        if (koin.getOrNull<ProjectConfigService>() == null) {
            val service = mock<ProjectConfigService>()
            every { service.configChanges } returns emptyFlow()
            everySuspend { service.reloadConfigs() } returns Unit
            everySuspend { service.getProjectConfig(any()) } calls { (projectPath: FolderPath) ->
                delay(state.responseDelay)
                state.configMap[projectPath] ?: throw IllegalStateException("No config for $projectPath")
            }
            koinAddObject<ProjectConfigService>(service)
        }
        return state
    }

    private class ProjectConfigServiceMockState {
        var responseDelay: Duration = Duration.ZERO
        var configMap: Map<FolderPath, ProjectConfig> = emptyMap()
        var configChanges: MutableSharedFlow<Unit>? = null
    }

    private fun toProjectConfig(fileNames: List<String>): ProjectConfig {
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

    private fun toConfigMap(projectConfigs: List<Models.ProjectConfig>): Map<FolderPath, ProjectConfig> {
        return projectConfigs.associate { config ->
            FolderPath(config.requestedProjectPath.toPath()) to toProjectConfig(config.returnedProjFiles)
        }
    }
}


object NullReturningGlobalProjectConfig : GlobalProjectConfigService {
    override suspend fun readConfig(): GlobalProjectConfig? = null
    override fun invalidateCache() = Unit
    override suspend fun openConfigFile() = Unit
}

object NullReturningLocalProjectConfig : LocalProjectConfigService {
    override suspend fun readConfig(folder: FolderPath): LocalProjectConfig? = null
    override fun invalidateCache() = Unit
    override suspend fun openConfigFile(projectId: Long) = Unit
}
