package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns typed result`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'assure current project ready'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'fake user instructor'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'real model'`
import gtr.mpfocus.domain.model.core.Steps.`then model instructs user to`
import gtr.mpfocus.domain.model.repos.Steps.`given 'fake projects repo' returns current project`
import gtr.mpfocus.domain.model.repos.Steps.`given exists 'fake projects repo'`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake file system'`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake operating system'`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CoreActionsInternalsTest {

    @Test
    fun `assure current project ready`() = runTest {
        hotest {
            `given exists 'fake file system'`()
            `given exists 'fake operating system'`()
            `given exists 'fake user instructor'`()
            `given exists 'fake projects repo'`()
            `given exists 'real model'`()

            variants("current project state") {

                variant("already set") {
                    `given 'fake projects repo' returns current project`("any/path/to/project")
                    `when model executes 'assure current project ready'`()
                    `then model returns typed result`("success")
                }

                variant("not set and user doesn't set") {
                    `given 'fake projects repo' returns current project`(null, null)
                    `when model executes 'assure current project ready'`()
                    `then model instructs user to`("set current project")
                    `then model returns typed result`("error: no current project")
                }

                variant("not set and user sets") {
                    `given 'fake projects repo' returns current project`(null, "any/path/to/project")
                    `when model executes 'assure current project ready'`()
                    `then model instructs user to`("set current project")
                    `then model returns typed result`("success")
                }
            }
        }
    }
}
