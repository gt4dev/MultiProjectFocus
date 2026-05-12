package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.Flow

data class ProjectConfig(
    private val fileNames: Map<ProjectFile, String>
) {
    fun fileName(file: ProjectFile): String {
        return fileNames[file] ?: throw IllegalStateException("option $file has no file name")
    }
}

interface ProjectConfigService {

    suspend fun getProjectConfig(projectPath: FolderPath): ProjectConfig

    /**
     * 'config reload' concept:
     * - UI (from button 'reload configs') calls 'fun reloadConfigs'
     * - 'fun reloadConfigs' just invalidates any cached data (from files)
     * - and through 'configChanges: Flow<Unit>' informs users of config to reload themselves
     * - then users (e.g. 'ProjectReadModel') reloads its data
     */
    suspend fun reloadConfigs()
    val configChanges: Flow<Unit>
}
