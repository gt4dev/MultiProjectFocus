package gtr.mpfocus.domain.model.read

import dev.hotest.Async.hotest
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'project config service mock' can mimic config reloads`
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'project config service mock' has observable 'config changes'`
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'project config service mock' returns project configs`
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`when 'project config service' is asked to reload configs`
import gtr.mpfocus.domain.model.config.Models.ProjectConfig
import gtr.mpfocus.domain.model.core.Models
import gtr.mpfocus.domain.model.read.Models.BuiltProject
import gtr.mpfocus.domain.model.read.ProjectReadModelSteps.`'project read model' stops collecting and ends the test`
import gtr.mpfocus.domain.model.read.ProjectReadModelSteps.`then 'project read model' subsequently publishes built projects list`
import gtr.mpfocus.domain.model.read.ProjectReadModelSteps.`when 'project read model' is called to build pinned projects list`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns pinned projects`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class ProjectReadModelTest {

    @Test
    fun `'pinned projects' data is correctly built over time, as consecutive pieces of actual project configs are read`() = runTest {
        hotest {

            `given 'project repository mock' returns pinned projects`(
                Models.Project(id = 101, path = "myPath/proj1", pinPosition = 1),
                Models.Project(id = 102, path = "myPath/proj2", pinPosition = 2),
                Models.Project(id = 103, path = "myPath/proj3", pinPosition = 3),
            )

            `given 'project config service mock' returns project configs`(
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

            `when 'project read model' is called to build pinned projects list`()

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

    @Test
    fun `'pinned projects' data is rebuilt when project configs are reloaded`() = runTest {
        hotest {

            `given 'project repository mock' returns pinned projects`(
                Models.Project(id = 101, path = "myPath/proj1", pinPosition = 1),
                Models.Project(id = 102, path = "myPath/proj2", pinPosition = 2),
            )

            `given 'project config service mock' returns project configs`(
                responseDelay = 1.seconds,
                projectConfigs = listOf(
                    ProjectConfig(
                        "myPath/proj1",
                        listOf("old-P1F1", "old-P1F2", "old-P1F3", "old-P1F4", "old-P1F5", "old-P1F6", "old-P1F7", "old-P1F8", "old-P1F9", "old-P1F0"),
                    ),
                    ProjectConfig(
                        "myPath/proj2",
                        listOf("old-P2F1", "old-P2F2", "old-P2F3", "old-P2F4", "old-P2F5", "old-P2F6", "old-P2F7", "old-P2F8", "old-P2F9", "old-P2F0"),
                    ),
                ),
            )

            `given 'project config service mock' has observable 'config changes'`()

            `given 'project config service mock' can mimic config reloads`()

            `when 'project read model' is called to build pinned projects list`()

            val defaultFileNames = listOf("File0.md", "File1.md", "File2.md", "File3.md", "File4.md", "File5.md", "File6.md", "File7.md", "File8.md", "File9.md")

            val oldProj1FileNames = listOf("old-P1F1", "old-P1F2", "old-P1F3", "old-P1F4", "old-P1F5", "old-P1F6", "old-P1F7", "old-P1F8", "old-P1F9", "old-P1F0")
            val oldProj2FileNames = listOf("old-P2F1", "old-P2F2", "old-P2F3", "old-P2F4", "old-P2F5", "old-P2F6", "old-P2F7", "old-P2F8", "old-P2F9", "old-P2F0")

            `then 'project read model' subsequently publishes built projects list`(
                listOf(
                    BuiltProject(path = "myPath/proj1", id = 101, files = defaultFileNames),
                    BuiltProject(path = "myPath/proj2", id = 102, files = defaultFileNames),
                ),
                listOf(
                    BuiltProject("myPath/proj1", 101, oldProj1FileNames),
                    BuiltProject("myPath/proj2", 102, defaultFileNames),
                ),
                listOf(
                    BuiltProject("myPath/proj1", 101, oldProj1FileNames),
                    BuiltProject("myPath/proj2", 102, oldProj2FileNames),
                ),
            )

            `given 'project config service mock' returns project configs`(
                responseDelay = 1.seconds,
                projectConfigs = listOf(
                    ProjectConfig(
                        "myPath/proj1",
                        listOf("new-P1F1", "new-P1F2", "new-P1F3", "new-P1F4", "new-P1F5", "new-P1F6", "new-P1F7", "new-P1F8", "new-P1F9", "new-P1F0"),
                    ),
                    ProjectConfig(
                        "myPath/proj2",
                        listOf("new-P2F1", "new-P2F2", "new-P2F3", "new-P2F4", "new-P2F5", "new-P2F6", "new-P2F7", "new-P2F8", "new-P2F9", "new-P2F0"),
                    ),
                ),
            )

            `when 'project config service' is asked to reload configs`()

            val newProj1FileNames = listOf("new-P1F1", "new-P1F2", "new-P1F3", "new-P1F4", "new-P1F5", "new-P1F6", "new-P1F7", "new-P1F8", "new-P1F9", "new-P1F0")
            val newProj2FileNames = listOf("new-P2F1", "new-P2F2", "new-P2F3", "new-P2F4", "new-P2F5", "new-P2F6", "new-P2F7", "new-P2F8", "new-P2F9", "new-P2F0")

            `then 'project read model' subsequently publishes built projects list`(
                listOf(
                    BuiltProject("myPath/proj1", 101, defaultFileNames),
                    BuiltProject("myPath/proj2", 102, defaultFileNames),
                ),
                listOf(
                    BuiltProject("myPath/proj1", 101, newProj1FileNames),
                    BuiltProject("myPath/proj2", 102, defaultFileNames),
                ),
                listOf(
                    BuiltProject("myPath/proj1", 101, newProj1FileNames),
                    BuiltProject("myPath/proj2", 102, newProj2FileNames),
                ),
            )

            `'project read model' stops collecting and ends the test`()
        }
    }
}
