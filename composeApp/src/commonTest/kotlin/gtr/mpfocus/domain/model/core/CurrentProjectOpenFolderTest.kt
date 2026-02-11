package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.Steps.`given exists 'basic config service'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'action preferences'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'fake user notifier'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'real model'`
import gtr.mpfocus.domain.model.core.Steps.`then model notify user to`
import gtr.mpfocus.domain.model.core.Steps.`then model returns`
import gtr.mpfocus.domain.model.core.Steps.`when model executes command 'open folder in current project'`
import gtr.mpfocus.domain.model.repos.Steps.`given exists 'fake projects repo'`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that each folder`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that folder is created successfully`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake file system'`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake operating system'`
import gtr.mpfocus.system_actions.Steps.`then 'fake file system' checks folder path exist'`
import gtr.mpfocus.system_actions.Steps.`then 'fake file system' creates folder`
import gtr.mpfocus.system_actions.Steps.`then 'fake operating system' opens folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CurrentProjectOpenFolderTest {

    @Test
    fun `open current project folder - but folder doesn't exist`() = runTest {
        hotest {
            `given exists 'fake file system'`()
            `given exists 'fake operating system'`()
            `given exists 'fake user notifier'`()
            `given exists 'fake projects repo'`(
                "any/path/to/project"
            )
            `given exists 'basic config service'`()
            `given exists 'real model'`()

            variants("user preference if no folder") {

                variant("auto-create") {
                    `given 'fake file system' returns that each folder`("doesn't exist")
                    `given exists 'action preferences'`("auto create")
                    `given 'fake file system' returns that folder is created successfully`()
                    `when model executes command 'open folder in current project'`()
                    `then 'fake file system' checks folder path exist'`()
                    `then 'fake file system' creates folder`()
                    `then 'fake operating system' opens folder`()
                    `then model returns`("success")
                }

                variant("report error") {
                    `given 'fake file system' returns that each folder`("doesn't exist")
                    `given exists 'action preferences'`("report error")
                    `when model executes command 'open folder in current project'`()
                    `then 'fake file system' checks folder path exist'`()
                    `then model returns`("error: folder doesn't exist")
                }

                variant("notify user") {
                    `given exists 'action preferences'`("notify user")

                    variants("user actions after notification") {

                        variant("user creates folder") {
                            `given 'fake file system' returns that each folder`("doesn't exist", "exists")
                            `when model executes command 'open folder in current project'`()
                            `then 'fake file system' checks folder path exist'`()
                            `then model notify user to`("create folder")
                            `then 'fake file system' checks folder path exist'`()
                            `then 'fake operating system' opens folder`()
                            `then model returns`("success")
                        }

                        variant("user doesn't create folder") {
                            `given 'fake file system' returns that each folder`("doesn't exist", "doesn't exist")
                            `when model executes command 'open folder in current project'`()
                            `then 'fake file system' checks folder path exist'`()
                            `then model notify user to`("create folder")
                            `then 'fake file system' checks folder path exist'`()
                            `then model returns`("error: folder doesn't exist")
                        }
                    }
                }
            }
        }
    }
}
