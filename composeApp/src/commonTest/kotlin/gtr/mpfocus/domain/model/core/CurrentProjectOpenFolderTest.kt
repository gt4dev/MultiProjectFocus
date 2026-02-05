package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.variant
import gtr.hotest.variants.variants
import gtr.mpfocus.domain.model.core.Steps.`given action preference 'if no folder' is`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' claims folder doesn't exist`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' claims folder was created successfully`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CurrentProjectOpenFolderTest {

    @Test
    fun `main scenario`() = runTest {
        hotest {
            println("gtr - start")
            `given 'fake file system' claims folder doesn't exist`()
            `given 'fake file system' claims folder was created successfully`()

            variants("user preference if no folder") {

                variant("auto-create") {
                    println("Creating auto-create folder")
                    `given action preference 'if no folder' is`("auto create")
                }

                variant("report error") {
                    println("Creating report error")
                    `given action preference 'if no folder' is`("report error")
                }

                variant("ask user") {
                    println("Creating ask user")
                    `given action preference 'if no folder' is`("ask user")
                }

            }

            println("gtr - end")
        }
    }
}