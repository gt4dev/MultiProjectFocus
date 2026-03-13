package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'basic config service' exists`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns Result`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns file path`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`then model returns folder path`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'ensure current project ready'`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'ensure project file ready'`
import gtr.mpfocus.domain.model.core.CoreActionsInternalsSteps.`when model executes 'ensure project folder ready'`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given 'sample project' has`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given 'user notifier mock' exists`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given exists 'action preferences'`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given exists 'real model'`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`then model notify user to`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' sequentially returns current project`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that file is created successfully`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that folder is created successfully`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' sequentially returns that file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' sequentially returns that folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'operating system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' creates file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' creates folder`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CoreActionsInternalsTest {

    @Test
    fun `assure current project ready`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`()
            `given 'basic config service' exists`()
            `given exists 'real model'`()

            variants("current project state") {

                variant("already set") {
                    `given 'project repository mock' sequentially returns current project`("any/path/to/project")
                    `when model executes 'ensure current project ready'`()
                    `then model returns Result`("success")
                }

                variant("not set and user doesn't set") {
                    `given 'project repository mock' sequentially returns current project`(null, null)
                    `when model executes 'ensure current project ready'`()
                    `then model notify user to`("set current project")
                    `then model returns Result`("error: no current project")
                }

                variant("not set and user sets") {
                    `given 'project repository mock' sequentially returns current project`(null, "any/path/to/project")
                    `when model executes 'ensure current project ready'`()
                    `then model notify user to`("set current project")
                    `then model returns Result`("success")
                }
            }
        }
    }

    @Test
    fun `ensure project file ready`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`()
            `given 'sample project' has`("any/path/to/project")
            `given 'basic config service' exists`()
            `given exists 'real model'`()

            variants("file state") {

                variant("file already exists") {
                    `given exists 'action preferences'`("report error")
                    `given 'file system mock' returns that file`("exists")
                    `when model executes 'ensure project file ready'`(ProjectFile.File1)
                    `then model returns Result`("success")
                    `then model returns file path`("any/path/to/project/main.md")
                }

                variant("file missing") {

                    variants("user preference") {

                        variant("report error") {
                            `given exists 'action preferences'`("report error")
                            `given 'file system mock' returns that file`("doesn't exist")
                            `when model executes 'ensure project file ready'`(ProjectFile.File1)
                            `then model returns Result`("error: no file exists")
                        }

                        variant("notify user") {
                            `given exists 'action preferences'`("notify user")
                            `given 'file system mock' sequentially returns that file`("doesn't exist", "exists")
                            `when model executes 'ensure project file ready'`(ProjectFile.File1)
                            `then model notify user to`("create file")
                            `then model returns Result`("success")
                        }

                        variant("auto create") {
                            `given exists 'action preferences'`("auto create")
                            `given 'file system mock' sequentially returns that file`("doesn't exist", "exists")
                            `given 'file system mock' returns that file is created successfully`()
                            `when model executes 'ensure project file ready'`(ProjectFile.File1)
                            `then 'file system mock' creates file`()
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
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`()
            `given 'sample project' has`("any/path/to/project")
            `given 'basic config service' exists`()
            `given exists 'real model'`()

            variants("folder state") {

                variant("folder already exists") {
                    `given exists 'action preferences'`("report error")
                    `given 'file system mock' returns that folder`("exists")
                    `when model executes 'ensure project folder ready'`()
                    `then model returns Result`("success")
                    `then model returns folder path`("any/path/to/project")
                }

                variant("folder missing") {

                    variants("user preference") {

                        variant("report error") {
                            `given exists 'action preferences'`("report error")
                            `given 'file system mock' returns that folder`("doesn't exist")
                            `when model executes 'ensure project folder ready'`()
                            `then model returns Result`("error: no project folder")
                        }

                        variant("notify user") {
                            `given exists 'action preferences'`("notify user")
                            `given 'file system mock' sequentially returns that folder`("doesn't exist", "exists")
                            `when model executes 'ensure project folder ready'`()
                            `then model notify user to`("create folder")
                            `then model returns Result`("success")
                        }

                        variant("auto create") {
                            `given exists 'action preferences'`("auto create")
                            `given 'file system mock' sequentially returns that folder`("doesn't exist", "exists")
                            `given 'file system mock' returns that folder is created successfully`()
                            `when model executes 'ensure project folder ready'`()
                            `then 'file system mock' creates folder`()
                            `then model returns Result`("success")
                        }
                    }
                }
            }
        }
    }
}
