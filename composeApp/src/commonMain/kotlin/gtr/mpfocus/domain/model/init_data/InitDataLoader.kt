package gtr.mpfocus.domain.model.init_data

import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FolderPath
import okio.Path.Companion.toPath

class InitDataLoader(
    val projectRepository: ProjectRepository
) {
    suspend fun loadData(tomlProject: ProjectToml) {
        projectRepository.deleteAll()

        val aliasToProjectId = mutableMapOf<String, Long>()

        tomlProject.projects.forEach { (alias, path) ->
            val projectId = projectRepository.addProject(FolderPath(path.toPath()))
            aliasToProjectId[alias] = projectId
        }

        tomlProject.currentProject?.let { currentProjectAlias ->
            aliasToProjectId[currentProjectAlias]?.let { currentProjectId ->
                projectRepository.setCurrentProject(currentProjectId)
            }
        }

        tomlProject.pinnedProjects.entries.forEachIndexed { index, (pinAlias, projectAlias) ->
            val projectId = aliasToProjectId[projectAlias] ?: return@forEachIndexed
            val pinPosition = pinAlias.removePrefix("pin").toIntOrNull() ?: index
            projectRepository.pinProject(projectId, pinPosition)
        }
    }
}
