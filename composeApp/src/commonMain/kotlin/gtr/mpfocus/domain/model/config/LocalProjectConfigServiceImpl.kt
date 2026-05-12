package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.config.project_config.LocalTomlFileActions
import gtr.mpfocus.domain.model.config.project_config.LocalTomlParser
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.system_actions.OperatingSystemActions

class LocalProjectConfigServiceImpl(
    private val projectRepository: ProjectRepository,
    private val localTomlFileActions: LocalTomlFileActions,
    private val localTomlParser: LocalTomlParser,
    private val operatingSystemActions: OperatingSystemActions,
) : LocalProjectConfigService {

    override suspend fun readConfig(folder: FolderPath): LocalProjectConfig? {
        val content = try {
            if (!localTomlFileActions.fileExists(folder)) {
                return null
            }
            localTomlFileActions.readFileContent(folder)
        } catch (e: Exception) {
            logReadConfigError(folder, "${e::class.simpleName ?: "Exception"}: ${e.message ?: "[no additional message]"}")
            return null
        }

        val config = localTomlParser.parseConfig(content)
        if (config == null) {
            logReadConfigError(folder, "Failed to parse TOML file.")
        }
        return config
    }

    override fun invalidateCache() {
        // no cache yet
    }

    override suspend fun openConfigFile(projectId: Long) {
        try {
            val project = projectRepository.getProject(projectId)
            if (project == null) {
                logOpenConfigFileError(projectId, "Project not found.")
                return
            }

            val projectFolder = project.folderPath
            if (!localTomlFileActions.fileExists(projectFolder)) {
                localTomlFileActions.createFile(projectFolder)
            }
            operatingSystemActions.openFile(localTomlFileActions.filePath(projectFolder))
        } catch (e: Exception) {
            logOpenConfigFileError(
                projectId = projectId,
                message = "${e::class.simpleName ?: "Exception"}: ${e.message ?: "[no additional message]"}",
            )
        }
    }

    private fun logOpenConfigFileError(projectId: Long, message: String) {
        println("Failed to open local project config for projectId=$projectId. $message")
    }

    private fun logReadConfigError(folder: FolderPath, message: String) {
        println("Failed to read local project config for folder=${folder.path}. $message")
    }
}
