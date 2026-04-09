package gtr.mpfocus.domain.model.config

import dev.hotest.Async.hotest
import dev.hotest.variants.Async.variant
import dev.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.Models.File
import gtr.mpfocus.domain.model.config.ProjectConfigServiceSteps.`given project with 'folder path' defines locally following files`
import gtr.mpfocus.domain.model.config.ProjectConfigServiceSteps.`given user defines globally following files`
import gtr.mpfocus.domain.model.config.ProjectConfigServiceSteps.`then 'project config service' returns project config with files`
import gtr.mpfocus.domain.model.config.ProjectConfigServiceSteps.`when 'project config service' is called for project`
import gtr.mpfocus.domain.model.core.ProjectFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProjectConfigServiceTest {

    @Test
    fun `'project config service' merges local, global, and default configs by priority`() = runTest {
        hotest {

            `given project with 'folder path' defines locally following files`(
                folderPath = "myPath/proj1",
                files = listOf(
                    File(id = ProjectFile.File1, name = "Proj1File1.md"),
                    File(id = ProjectFile.File2, name = "Proj1File2.md"),
                ),
            )

            `given project with 'folder path' defines locally following files`(
                folderPath = "myPath/proj2",
                files = listOf(
                    File(id = ProjectFile.File3, name = "Proj2File3.md"),
                    File(id = ProjectFile.File4, name = "Proj2File4.md"),
                ),
            )

            `given user defines globally following files`(
                files = listOf(
                    File(id = ProjectFile.File1, name = "GlobalFile1.md"),
                    File(id = ProjectFile.File2, name = "GlobalFile2.md"),
                    File(id = ProjectFile.File3, name = "GlobalFile3.md"),
                    File(id = ProjectFile.File4, name = "GlobalFile4.md"),
                    File(id = ProjectFile.File5, name = "GlobalFile5.md"),
                ),
            )

            variants("test for project path =") {

                variant("myPath/proj1") {

                    `when 'project config service' is called for project`("myPath/proj1")

                    `then 'project config service' returns project config with files`(
                        // File0: default (ordinal 0)
                        "file0.md",
                        // File1, File2: local overrides global
                        "Proj1File1.md", "Proj1File2.md",
                        // File3, File4, File5: global overrides default
                        "GlobalFile3.md", "GlobalFile4.md", "GlobalFile5.md",
                        // File6..File9: default
                        "file6.md", "file7.md", "file8.md", "file9.md",
                    )
                }

                variant("myPath/proj2") {
                    `when 'project config service' is called for project`("myPath/proj2")

                    `then 'project config service' returns project config with files`(
                        // File0: default
                        "file0.md",
                        // File1, File2: global overrides default
                        "GlobalFile1.md", "GlobalFile2.md",
                        // File3, File4: local overrides global
                        "Proj2File3.md", "Proj2File4.md",
                        // File5: global overrides default
                        "GlobalFile5.md",
                        // File6..File9: default
                        "file6.md", "file7.md", "file8.md", "file9.md",
                    )
                }

                variant("myPath/proj3 - without local definition") {
                    `when 'project config service' is called for project`("myPath/proj3")

                    `then 'project config service' returns project config with files`(
                        // File0: default
                        "file0.md",
                        // File1..File5: global overrides default
                        "GlobalFile1.md", "GlobalFile2.md", "GlobalFile3.md", "GlobalFile4.md", "GlobalFile5.md",
                        // File6..File9: default
                        "file6.md", "file7.md", "file8.md", "file9.md",
                    )
                }
            }
        }
    }
}
