package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.Steps.`given exists 'basic config service'`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns Result`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns file path`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns folder path`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'assure current project ready'`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'ensure project file ready'`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'ensure project folder ready'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'action preferences'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'fake user notifier'`
import gtr.mpfocus.domain.model.core.Steps.`given exists 'real model'`
import gtr.mpfocus.domain.model.core.Steps.`then model notify user to`
import gtr.mpfocus.domain.model.repos.Steps.`given exists 'fake projects repo'`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that each file`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that each folder`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that file is created successfully`
import gtr.mpfocus.system_actions.Steps.`given 'fake file system' returns that folder is created successfully`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake file system'`
import gtr.mpfocus.system_actions.Steps.`given exists 'fake operating system'`
import gtr.mpfocus.system_actions.Steps.`then 'fake file system' creates file`
import gtr.mpfocus.system_actions.Steps.`then 'fake file system' creates folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CoreActionsInternalsTest {

    @Test
    fun `assure current project ready`() = runTest {
        hotest {
            `given exists 'fake file system'`()
            `given exists 'fake operating system'`()
            `given exists 'fake user notifier'`()
            `given exists 'fake projects repo'`()
            `given exists 'basic config service'`()
            `given exists 'real model'`()

            variants("current project state") {

                variant("already set") {
                    `given exists 'fake projects repo'`("any/path/to/project")
                    `when model executes 'assure current project ready'`()
                    `then model returns Result`("success")
                }

                variant("not set and user doesn't set") {
                    `given exists 'fake projects repo'`(null, null)
                    `when model executes 'assure current project ready'`()
                    `then model notify user to`("set current project")
                    `then model returns Result`("error: no current project")
                }

                variant("not set and user sets") {
                    `given exists 'fake projects repo'`(null, "any/path/to/project")
                    `when model executes 'assure current project ready'`()
                    `then model notify user to`("set current project")
                    `then model returns Result`("success")
                }
            }
        }
    }

    @Test
    fun `ensure project file ready`() = runTest {
        hotest {
            `given exists 'fake file system'`()
            `given exists 'fake operating system'`()
            `given exists 'fake user notifier'`()
            `given exists 'fake projects repo'`()
            `given exists 'fake projects repo'`("any/path/to/project")
            `given exists 'basic config service'`()
            `given exists 'real model'`()

            variants("file state") {

                variant("file already exists") {
                    `given exists 'action preferences'`("report error")
                    `given 'fake file system' returns that each file`("exists")
                    `when model executes 'ensure project file ready'`(ProjectFiles.File0)
                    `then model returns Result`("success")
                    `then model returns file path`("any/path/to/project/main.md")
                }

                variant("file missing") {

                    variants("user preference") {

                        variant("report error") {
                            `given exists 'action preferences'`("report error")
                            `given 'fake file system' returns that each file`("doesn't exist")
                            `when model executes 'ensure project file ready'`(ProjectFiles.File0)
                            `then model returns Result`("error: no file exists")
                        }

                        variant("notify user") {
                            `given exists 'action preferences'`("notify user")
                            `given 'fake file system' returns that each file`("doesn't exist", "exists")
                            `when model executes 'ensure project file ready'`(ProjectFiles.File0)
                            `then model notify user to`("create file")
                            `then model returns Result`("success")
                        }

                        variant("auto create") {
                            `given exists 'action preferences'`("auto create")
                            `given 'fake file system' returns that each file`("doesn't exist", "exists")
                            `given 'fake file system' returns that file is created successfully`()
                            `when model executes 'ensure project file ready'`(ProjectFiles.File0)
                            `then 'fake file system' creates file`()
                            `then model returns Result`("success")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `ensure project folder ready`() = runTest {
        hotest {
            `given exists 'fake file system'`()
            `given exists 'fake operating system'`()
            `given exists 'fake user notifier'`()
            `given exists 'fake projects repo'`("any/path/to/project")
            `given exists 'basic config service'`()
            `given exists 'real model'`()

            variants("folder state") {

                variant("folder already exists") {
                    `given exists 'action preferences'`("report error")
                    `given 'fake file system' returns that each folder`("exists")
                    `when model executes 'ensure project folder ready'`()
                    `then model returns Result`("success")
                    `then model returns folder path`("any/path/to/project")
                }

                variant("folder missing") {

                    variants("user preference") {

                        variant("report error") {
                            `given exists 'action preferences'`("report error")
                            `given 'fake file system' returns that each folder`("doesn't exist")
                            `when model executes 'ensure project folder ready'`()
                            `then model returns Result`("error: no project folder")
                        }

                        variant("notify user") {
                            `given exists 'action preferences'`("notify user")
                            `given 'fake file system' returns that each folder`("doesn't exist", "exists")
                            `when model executes 'ensure project folder ready'`()
                            `then model notify user to`("create folder")
                            `then model returns Result`("success")
                        }

                        variant("auto create") {
                            `given exists 'action preferences'`("auto create")
                            `given 'fake file system' returns that each folder`("doesn't exist", "exists")
                            `given 'fake file system' returns that folder is created successfully`()
                            `when model executes 'ensure project folder ready'`()
                            `then 'fake file system' creates folder`()
                            `then model returns Result`("success")
                        }
                    }
                }
            }
        }
    }
}
