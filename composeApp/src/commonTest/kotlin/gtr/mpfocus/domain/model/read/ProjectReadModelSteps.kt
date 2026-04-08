package gtr.mpfocus.domain.model.read

import dev.hotest.HOTestCtx
import gtr.mpfocus.hotest.koinAddObject
import kotlinx.coroutines.flow.toList
import kotlin.test.assertEquals

object Models {
    data class BuiltProject(
        val path: String,
        val id: Long,
        val files: List<String>,
    )
}

object ProjectReadModelSteps {


    private data class ProjectsHistoryHolder(val history: List<List<ProjectWithFileNames>>)

    suspend fun HOTestCtx.`when 'project read model' is called for build pinned project list`() {
        val projectReadModel = ProjectReadModelImpl(
            projectRepository = koin.get(),
            projectConfigService = koin.get(),
        )
        val pinnedProjectsHistory = projectReadModel.getPinnedProjects().toList()
        koinAddObject(ProjectsHistoryHolder(pinnedProjectsHistory))
    }

    fun HOTestCtx.`then 'project read model' subsequently publishes built projects list`(
        vararg expectedProjects: List<Models.BuiltProject>,
    ) {
        val actualProjectsHistory = koin.get<ProjectsHistoryHolder>().history

        val actualFileNamesHistory = actualProjectsHistory.map { actualProjects ->
            actualProjects.map { actualProject ->
                actualProject.fileNames.map { fn ->
                    fn.fileName
                }
            }
        }
        val expectedFileNamesHistory = expectedProjects.map { expectedProject ->
            expectedProject.map { it.files }
        }
        assertEquals(expectedFileNamesHistory, actualFileNamesHistory)
    }
}
