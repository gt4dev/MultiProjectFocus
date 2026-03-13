package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.Steps.`given exists 'basic config service'`
import gtr.mpfocus.domain.model.core.Steps.`given 'user notifier mock' exists`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'action preferences'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'real model'`
import gtr.mpfocus.domain.model.core.Steps.`then model notify user to`
import gtr.mpfocus.domain.model.core.Steps.`then model returns`
import gtr.mpfocus.domain.model.core.Steps.`when model executes command 'open folder in current project'`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' sequentially returns current project`
import gtr.mpfocus.system_actions.Steps.`given 'file system mock' exists`
import gtr.mpfocus.system_actions.Steps.`given 'file system mock' returns that folder`
import gtr.mpfocus.system_actions.Steps.`given 'file system mock' returns that folder is created successfully`
import gtr.mpfocus.system_actions.Steps.`given 'file system mock' sequentially returns that folder`
import gtr.mpfocus.system_actions.Steps.`given 'operating system mock' exists`
import gtr.mpfocus.system_actions.Steps.`then 'file system mock' checks folder path exist'`
import gtr.mpfocus.system_actions.Steps.`then 'file system mock' creates folder`
import gtr.mpfocus.system_actions.Steps.`then 'operating system mock' opens folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CurrentProjectOpenFolderTest {

    @Test
    fun `open current project folder - but folder doesn't exist`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`(
                "any/path/to/project"
            )
            `given exists 'basic config service'`()
            `given exists 'real model'`()

            variants("user preference if no folder") {

                variant("auto-create") {
                    `given 'file system mock' sequentially returns that folder`("doesn't exist", "exists")
                    `given exists 'action preferences'`("auto create")
                    `given 'file system mock' returns that folder is created successfully`()
                    `when model executes command 'open folder in current project'`()
                    `then 'file system mock' checks folder path exist'`()
                    `then 'file system mock' creates folder`()
                    `then 'file system mock' checks folder path exist'`()
                    `then 'operating system mock' opens folder`()
                    `then model returns`("success")
                }

                variant("report error") {
                    `given 'file system mock' returns that folder`("doesn't exist")
                    `given exists 'action preferences'`("report error")
                    `when model executes command 'open folder in current project'`()
                    `then 'file system mock' checks folder path exist'`()
                    `then model returns`("error: no project folder")
                }

                variant("notify user") {
                    `given exists 'action preferences'`("notify user")

                    variants("user actions after notification") {

                        variant("user creates folder") {
                            `given 'file system mock' sequentially returns that folder`("doesn't exist", "exists")
                            `when model executes command 'open folder in current project'`()
                            `then 'file system mock' checks folder path exist'`()
                            `then model notify user to`("create folder")
                            `then 'file system mock' checks folder path exist'`()
                            `then 'operating system mock' opens folder`()
                            `then model returns`("success")
                        }

                        variant("user doesn't create folder") {
                            `given 'file system mock' sequentially returns that folder`(
                                "doesn't exist",
                                "doesn't exist"
                            )
                            `when model executes command 'open folder in current project'`()
                            `then 'file system mock' checks folder path exist'`()
                            `then model notify user to`("create folder")
                            `then 'file system mock' checks folder path exist'`()
                            `then model returns`("error: no project folder")
                        }
                    }
                }
            }
        }
    }
}
