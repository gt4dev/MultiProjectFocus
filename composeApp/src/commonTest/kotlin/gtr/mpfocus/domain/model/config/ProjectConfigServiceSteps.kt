package gtr.mpfocus.domain.model.config

import dev.hotest.HOTestCtx
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.hotest.koinAddIfMissing
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.system_actions.FolderPath
import okio.Path.Companion.toPath
import kotlin.test.assertEquals


object ProjectConfigServiceSteps {

    fun HOTestCtx.`given project with 'folder path' defines locally following files`(
        folderPath: String,
        files: List<Models.File>,
    ) {
        val service = koinAddIfMissing {
            mock<LocalProjectConfigService>().also { service ->
                // "default" returns null
                everySuspend {
                    service.readConfig(any())
                } returns null
            }
        }
        everySuspend {
            // "this" returns non-null
            service.readConfig(FolderPath(folderPath.toPath()))
        } returns LocalProjectConfig(
            fileNames = files.associate { it.id to it.name }
        )
    }

    fun HOTestCtx.`given user defines globally following files`(
        files: List<Models.File>,
    ) {
        val globalConfig = GlobalProjectConfig(
            fileNames = files.associate { it.id to it.name }
        )
        val service = mock<GlobalProjectConfigService>()
        everySuspend { service.readConfig() } returns globalConfig
        koinAddObject<GlobalProjectConfigService>(service)
    }

    suspend fun HOTestCtx.`when 'project config service' is called for project`(projectPath: String) {
        val service = ProjectConfigServiceImpl(
            globalProjectConfigService = koin.get(),
            localProjectConfigService = koin.get(),
        )
        val result = service.getProjectConfig(FolderPath(projectPath.toPath()))
        koinAddObject<ProjectConfig>(result)
    }

    fun HOTestCtx.`then 'project config service' returns project config with files`(
        vararg expectedFileNames: String,
    ) {
        val config = koin.get<ProjectConfig>()
        val actual = ProjectFile.entries.map { config.fileName(it) }
        assertEquals(expectedFileNames.toList(), actual)
    }
}
