package gtr.mpfocus.domain.repository

import gtr.hotest.Async.hotest
import gtr.mpfocus.domain.repository.Models.Project
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'real project repo' exists`
import gtr.mpfocus.domain.repository.RepositorySteps.`then 'project repo' current project is`
import gtr.mpfocus.domain.repository.RepositorySteps.`when 'project repo' sets current project`
import gtr.mpfocus.infra.db_repo.DBSteps.`'test database' sets up`
import gtr.mpfocus.infra.db_repo.DBSteps.`'test database' tears down`
import gtr.mpfocus.infra.db_repo.DBSteps.`given 'project dao' has data`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProjectRepositoryTest {

    @Test
    fun `set current project`() = runTest {
        hotest(
            beforeTest = {
                `'test database' sets up`()
            },
            afterTest = {
                `'test database' tears down`()
            },
        ) {
            `given 'project dao' has data`(
                Project(id = 1, path = "/path_A"),
                Project(id = 3, path = "/path_B"),
                Project(id = 5, path = "/path_C"),
            )
            `given 'real project repo' exists`()

            `then 'project repo' current project is`(null)

            `when 'project repo' sets current project`(id = 1)
            `then 'project repo' current project is`(Project(id = 1, path = "/path_A"))

            `when 'project repo' sets current project`(id = 3)
            `then 'project repo' current project is`(Project(id = 3, path = "/path_B"))

            `when 'project repo' sets current project`(id = 5)
            `then 'project repo' current project is`(Project(id = 5, path = "/path_C"))

            `when 'project repo' sets current project`(id = null)
            `then 'project repo' current project is`(null)
        }
    }
}
