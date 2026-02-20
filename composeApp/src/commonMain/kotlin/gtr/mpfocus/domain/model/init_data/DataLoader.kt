package gtr.mpfocus.domain.model.init_data

import com.akuleshov7.ktoml.Toml
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.serialization.decodeFromString
import okio.Path.Companion.toPath

class DataLoader(
    val projectRepository: ProjectRepository,
    val fileSystemActions: FileSystemActions
) {
    suspend fun loadData(tomlProject: FilePath) {
        val fileContent = fileSystemActions.readFile(tomlProject)
        loadData(fileContent)
    }

    suspend fun loadData(tomlProject: String) {
        val projectToml = Toml.decodeFromString<ProjectToml>(tomlProject)
        loadData(projectToml)
    }

    suspend fun loadData(tomlProject: ProjectToml) {
        projectRepository.deleteAll()

        val aliasToProjectId = mutableMapOf<String, Long>()

        tomlProject.projects.forEach { (alias, path) ->
            val projectId = projectRepository.addProject(FolderPath(path.toPath()))
            aliasToProjectId[alias] = projectId
        }

        tomlProject.currentProject?.let {
            requireNotNull(aliasToProjectId[it]).let { currentProjectId ->
                projectRepository.setCurrentProject(currentProjectId)
            }
        }

        tomlProject.pinnedProjects.entries.forEach { (pinAlias, projectAlias) ->
            val projectId = requireNotNull(aliasToProjectId[projectAlias])
            val pinPosition = pinAlias.removePrefix("pin").toInt()
            projectRepository.pinProject(projectId, pinPosition)
        }
    }
}
