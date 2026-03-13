package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`then model returns`
import gtr.mpfocus.domain.model.core.Models.Project
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'real project actions' exists`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open folder in current project'`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open folder in pinned project'`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open folder in regular project'`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns other projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns pinned projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' sequentially returns current project`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'operating system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'operating system mock' opens folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class OpenFolderTest {

    @Test
    fun `open project folder - happy day scenario`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'project repository mock' sequentially returns current project`()
            `given 'real project actions' exists`()

            variants("project type") {

                variant("current project") {
                    `given 'project repository mock' returns current project`(
                        Project(path = "path/to/current-project")
                    )
                    `given 'file system mock' returns that folder`("exists")

                    `when 'project actions' executes command 'open folder in current project'`()

                    `then 'operating system mock' opens folder`("path/to/current-project")
                    `then model returns`("success")
                }

                variant("pinned project") {
                    `given 'project repository mock' returns pinned projects`(
                        Project(id = 111, path = "any/path/project111", pinPosition = 1),
                        Project(id = 222, path = "any/path/project222", pinPosition = 2),
                        Project(id = 333, path = "any/path/project333", pinPosition = 3),
                        Project(id = 444, path = "any/path/project444", pinPosition = 4),
                        Project(id = 555, path = "any/path/project555", pinPosition = 5),
                    )
                    `given 'file system mock' returns that folder`("exists")

                    `when 'project actions' executes command 'open folder in pinned project'`(pinPosition = 3)

                    `then 'operating system mock' opens folder`("any/path/project333")
                    `then model returns`("success")
                }

                variant("other project") {
                    `given 'project repository mock' returns other projects`(
                        Project(id = 111, path = "any/path/project111", pinPosition = 1),
                        Project(id = 222, path = "any/path/project222", pinPosition = 2),
                        Project(id = 333, path = "any/path/project333", pinPosition = 3),
                        Project(id = 444, path = "any/path/project444", pinPosition = 4),
                        Project(id = 555, path = "any/path/project555", pinPosition = 5),
                    )
                    `given 'file system mock' returns that folder`("exists")

                    `when 'project actions' executes command 'open folder in regular project'`(projectId = 333)

                    `then 'operating system mock' opens folder`("any/path/project333")
                    `then model returns`("success")
                }
            }
        }
    }
}
