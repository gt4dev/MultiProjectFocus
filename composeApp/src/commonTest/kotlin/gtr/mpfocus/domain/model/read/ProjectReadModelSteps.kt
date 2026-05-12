package gtr.mpfocus.domain.model.read

import dev.hotest.HOTestCtx
import gtr.mpfocus.hotest.koinAddObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.assertEquals

object Models {
    data class BuiltProject(
        val path: String,
        val id: Long,
        val files: List<String>,
    )
}

object ProjectReadModelSteps {


    private class ProjectsHistoryCollector {
        val history = mutableListOf<List<ProjectWithFileNames>>()
        var collectionJob: Job? = null
        var checkedEmissionsCount = 0
    }

    suspend fun HOTestCtx.`when 'project read model' is called to build pinned projects list`() {
        val projectReadModel = ProjectReadModelImpl(
            projectRepository = koin.get(),
            projectConfigService = koin.get(),
        )
        val collector = ProjectsHistoryCollector()
        collector.collectionJob = CoroutineScope(currentCoroutineContext()).launch {
            projectReadModel.getPinnedProjects().collect { projects ->
                collector.history.add(projects)
            }
        }
        koinAddObject(collector)
    }

    suspend fun HOTestCtx.`then 'project read model' subsequently publishes built projects list`(
        vararg expectedProjects: List<Models.BuiltProject>,
    ) {
        val collector = koin.get<ProjectsHistoryCollector>()
        waitForProjectReadModelEmissionsEnd()
        assertProjectsHistory(
            expectedProjects = expectedProjects,
            actualProjectsHistory = collector.history.drop(collector.checkedEmissionsCount).take(expectedProjects.size),
        )
        collector.checkedEmissionsCount += expectedProjects.size
    }

    suspend fun HOTestCtx.`'project read model' stops collecting and ends the test`() {
        koin.get<ProjectsHistoryCollector>().collectionJob?.cancelAndJoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun waitForProjectReadModelEmissionsEnd() {
        // note: it's safe to use 'currentCoroutineContext'
        // neither 'parallel' tests use THIS 'currentCoroutineContext'
        val testDispatcher = currentCoroutineContext()[ContinuationInterceptor] as TestDispatcher
        testDispatcher.scheduler.advanceUntilIdle()
    }

    private fun assertProjectsHistory(
        expectedProjects: Array<out List<Models.BuiltProject>>,
        actualProjectsHistory: List<List<ProjectWithFileNames>>,
    ) {
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
