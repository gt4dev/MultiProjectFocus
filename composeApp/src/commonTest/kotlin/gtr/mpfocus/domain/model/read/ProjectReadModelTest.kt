package gtr.mpfocus.domain.model.read

import dev.hotest.Async.hotest
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'project config service mock' returns project config as follows`
import gtr.mpfocus.domain.model.config.Models.ProjectConfig
import gtr.mpfocus.domain.model.core.Models
import gtr.mpfocus.domain.model.read.Models.BuiltProject
import gtr.mpfocus.domain.model.read.ProjectReadModelSteps.`then 'project read model' subsequently publishes built projects list`
import gtr.mpfocus.domain.model.read.ProjectReadModelSteps.`when 'project read model' is called for build pinned project list`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns pinned projects`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class ProjectReadModelTest {

    @Test
    fun `'pinned projects' data is correctly build as there comes fresh data about actual project config`() = runTest {
        hotest {

            `given 'project repository mock' returns pinned projects`(
                Models.Project(id = 101, path = "myPath/proj1", pinPosition = 1),
                Models.Project(id = 102, path = "myPath/proj2", pinPosition = 2),
                Models.Project(id = 103, path = "myPath/proj3", pinPosition = 3),
            )

            `given 'project config service mock' returns project config as follows`(
                responseDelay = 1.seconds,
                projectConfigs = listOf(
                    ProjectConfig(
                        "myPath/proj1",
                        listOf("P1F1", "P1F2", "P1F3", "P1F4", "P1F5", "P1F6", "P1F7", "P1F8", "P1F9", "P1F0"),
                    ),
                    ProjectConfig(
                        "myPath/proj2",
                        listOf("P2F1", "P2F2", "P2F3", "P2F4", "P2F5", "P2F6", "P2F7", "P2F8", "P2F9", "P2F0"),
                    ),
                    ProjectConfig(
                        "myPath/proj3",
                        listOf("P3F1", "P3F2", "P3F3", "P3F4", "P3F5", "P3F6", "P3F7", "P3F8", "P3F9", "P3F0"),
                    ),
                ),
            )

            `when 'project read model' is called for build pinned project list`()

            val defaultFileNames = listOf("File0.md", "File1.md", "File2.md", "File3.md", "File4.md", "File5.md", "File6.md", "File7.md", "File8.md", "File9.md")

            val proj1FileNames = listOf("P1F1", "P1F2", "P1F3", "P1F4", "P1F5", "P1F6", "P1F7", "P1F8", "P1F9", "P1F0")
            val proj2FileNames = listOf("P2F1", "P2F2", "P2F3", "P2F4", "P2F5", "P2F6", "P2F7", "P2F8", "P2F9", "P2F0")
            val proj3FileNames = listOf("P3F1", "P3F2", "P3F3", "P3F4", "P3F5", "P3F6", "P3F7", "P3F8", "P3F9", "P3F0")

            `then 'project read model' subsequently publishes built projects list`(
                // 1st emission contains default file names
                listOf(
                    BuiltProject(path = "myPath/proj1", id = 101, files = defaultFileNames),
                    BuiltProject(path = "myPath/proj2", id = 102, files = defaultFileNames),
                    BuiltProject(path = "myPath/proj3", id = 103, files = defaultFileNames),
                ),
                // 2nd emission contains updated proj1 file names
                listOf(
                    BuiltProject("myPath/proj1", 101, proj1FileNames),
                    BuiltProject("myPath/proj2", 102, defaultFileNames),
                    BuiltProject("myPath/proj3", 103, defaultFileNames),
                ),
                // 3rd emission contains updated proj2 file names
                listOf(
                    BuiltProject("myPath/proj1", 101, proj1FileNames),
                    BuiltProject("myPath/proj2", 102, proj2FileNames),
                    BuiltProject("myPath/proj3", 103, defaultFileNames),
                ),
                // 4th emission contains updated proj3 file names
                listOf(
                    BuiltProject("myPath/proj1", 101, proj1FileNames),
                    BuiltProject("myPath/proj2", 102, proj2FileNames),
                    BuiltProject("myPath/proj3", 103, proj3FileNames),
                ),
            )
        }
    }
}
