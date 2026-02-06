package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.core.Steps.`given action preference 'if no folder' is`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'real model' service`
import gtr.mpfocus.domain.model.core.Steps.`then model returns success`
import gtr.mpfocus.domain.model.core.Steps.`when model executes command 'open folder in current project'`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' always returns that folder doesn't exist`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' always returns that folder is created successfully`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake operating system'`
import gtr.mpfocus.system_actions.Steps.`then 'fake file system' checks path exist'`
import gtr.mpfocus.system_actions.Steps.`then 'fake file system' creates folder`
import gtr.mpfocus.system_actions.Steps.`then 'fake operating system' opens folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CurrentProjectOpenFolderTest {

    @Test
    fun `open current project folder - but folder doesn't exist`() = runTest {
        hotest {

            `given 'fake file system' always returns that folder doesn't exist`()
            `given 'fake file system' always returns that folder is created successfully`()
            `given exists 'fake operating system'`()
            `given exists 'real model' service`()

            variants("user preference if no folder") {

                variant("auto-create") {
                    `given action preference 'if no folder' is`("auto create")
                    `then 'fake file system' checks path exist'`()
                    `when model executes command 'open folder in current project'`()
                    `then 'fake file system' checks path exist'`()
                    `then 'fake file system' creates folder`()
                    `then 'fake operating system' opens folder`()
                    `then model returns success`()
                }

//                variant("report error") {
//                    `given action preference 'if no folder' is`("report error")
//                    `when model executes command: open folder in current project`()
//                    `then 'fake file system' checks path existence'`()
//                    `then model returns error`("folder doesn't exist") // error("folder doesn't exist")
//                }

//                variant("ask user") {
//                    `given action preference 'if no folder' is`("ask user")
//
//                    variants("user response") {
//
//                        variant("answer = create") {
//                            `given 'fake user' respones`("create") // n: zakodowac step w 'core'
//                            `when model executes command: open folder in current project`()
//                            `then 'fake file system' checks path existence'`()
//                            `then model asks user for opinion`()
//                            `then 'fake file system' creates folder`()
//                            `then 'fake operating system' opens folder`()
//                            `then model returns success`()
//                        }
//
//                        variant("answer = cancel") {
//                            `given 'fake user' respones`("cancel")
//                            `when model executes command: open folder in current project`()
//                            `then 'fake file system' checks path existence'`()
//                            `then model asks user for opinion`()
//                            `then 'fake file system' creates folder`()
//                            `then 'fake operating system' opens folder`()
//                            `then model returns error`("operation cancelled")
//                        }
//                    }
//                }
            }
        }
    }
}