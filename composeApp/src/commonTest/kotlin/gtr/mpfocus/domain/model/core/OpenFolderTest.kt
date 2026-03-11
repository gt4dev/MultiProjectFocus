package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.core.Models.Project
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'real project actions' exists`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open folder in current project'`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open folder in pinned project'`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open folder in regular project'`
import gtr.mpfocus.domain.model.core.Steps.`then model returns`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake project repository' returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake project repository' sequentially returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake projects repo' returns other projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake projects repo' returns pinned projects`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that folder`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake file system'`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake operating system'`
import gtr.mpfocus.system_actions.Steps.`then 'fake operating system' opens folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class OpenFolderTest {

    @Test
    fun `open project folder - happy day scenario`() = runTest {
        hotest {
            `given exists 'fake file system'`()
            `given exists 'fake operating system'`()
            `given 'fake project repository' sequentially returns current project`()
            `given 'real project actions' exists`()

            variants("project type") {

                variant("current project") {
                    `given 'fake project repository' returns current project`(
                        Project(path = "path/to/current-project")
                    )
                    `given 'fake file system' returns that folder`("exists")

                    `when 'project actions' executes command 'open folder in current project'`()

                    `then 'fake operating system' opens folder`("path/to/current-project")
                    `then model returns`("success")
                }

                variant("pinned project") {
                    `given 'fake projects repo' returns pinned projects`(
                        Project(id = 111, path = "any/path/project111", pinPosition = 1),
                        Project(id = 222, path = "any/path/project222", pinPosition = 2),
                        Project(id = 333, path = "any/path/project333", pinPosition = 3),
                        Project(id = 444, path = "any/path/project444", pinPosition = 4),
                        Project(id = 555, path = "any/path/project555", pinPosition = 5),
                    )
                    `given 'fake file system' returns that folder`("exists")

                    `when 'project actions' executes command 'open folder in pinned project'`(pinPosition = 3)

                    `then 'fake operating system' opens folder`("any/path/project333")
                    `then model returns`("success")
                }

                variant("other project") {
                    `given 'fake projects repo' returns other projects`(
                        Project(id = 111, path = "any/path/project111", pinPosition = 1),
                        Project(id = 222, path = "any/path/project222", pinPosition = 2),
                        Project(id = 333, path = "any/path/project333", pinPosition = 3),
                        Project(id = 444, path = "any/path/project444", pinPosition = 4),
                        Project(id = 555, path = "any/path/project555", pinPosition = 5),
                    )
                    `given 'fake file system' returns that folder`("exists")

                    `when 'project actions' executes command 'open folder in regular project'`(projectId = 333)

                    `then 'fake operating system' opens folder`("any/path/project333")
                    `then model returns`("success")
                }
            }
        }
    }
}
