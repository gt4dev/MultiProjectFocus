package gtr.mpfocus.domain.model.core

import gtr.hotest.Async.hotest
import gtr.hotest.variants.Async.variant
import gtr.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'basic config service' exists`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given 'user notifier mock' exists`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given exists 'action preferences'`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`given exists 'real model'`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`then model notify user to`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`then model returns`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`when model executes command 'open file in current project'`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' sequentially returns current project`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that file is created successfully`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that folder is created successfully`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' sequentially returns that file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' sequentially returns that folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'operating system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' checks file path exist'`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' checks folder path exist'`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' creates file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' creates folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'operating system mock' opens file`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CurrentProjectOpenFileTest {

    @Test
    fun `open current project file - but no current project`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`(null, null)
            `given 'basic config service' exists`()
            `given exists 'real model'`()

            `given exists 'action preferences'`("report error")
            `when model executes command 'open file in current project'`(ProjectFile.File0)
            `then model notify user to`("set current project")
            `then model returns`("error: no current project")
        }
    }

    @Test
    fun `open current project file - but folder doesn't exist`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`("any/path/to/project")
            `given 'basic config service' exists`()
            `given exists 'real model'`()

            variants("user preference if no folder") {

                variant("auto-create") {
                    `given exists 'action preferences'`("auto create")
                    `given 'file system mock' sequentially returns that folder`("doesn't exist", "exists")
                    `given 'file system mock' returns that file`("exists")
                    `given 'file system mock' returns that folder is created successfully`()
                    `when model executes command 'open file in current project'`(ProjectFile.File0)
                    `then 'file system mock' checks folder path exist'`()
                    `then 'file system mock' creates folder`()
                    // todo: rename to ..exists
                    `then 'file system mock' checks folder path exist'`()
                    `then 'file system mock' checks file path exist'`()
                    `then 'operating system mock' opens file`()
                    `then model returns`("success")
                }

                variant("report error") {
                    `given exists 'action preferences'`("report error")
                    `given 'file system mock' returns that folder`("doesn't exist")
                    `when model executes command 'open file in current project'`(ProjectFile.File0)
                    `then 'file system mock' checks folder path exist'`()
                    `then model returns`("error: no project folder")
                }

                variant("notify user") {

                    `given exists 'action preferences'`("notify user")

                    variants("user reactions after notification") {

                        variant("user creates folder") {
                            `given 'file system mock' sequentially returns that folder`("doesn't exist", "exists")
                            `given 'file system mock' returns that file`("exists")
                            `when model executes command 'open file in current project'`(ProjectFile.File0)
                            `then 'file system mock' checks folder path exist'`()
                            `then model notify user to`("create folder")
                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`()
                            `then model returns`("success")
                        }

                        variant("user doesn't create folder") {
                            `given 'file system mock' sequentially returns that folder`(
                                "doesn't exist",
                                "doesn't exist"
                            )
                            `when model executes command 'open file in current project'`(ProjectFile.File0)
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

    @Test
    fun `open current project file - but file doesn't exist`() = runTest {
        hotest {
            `given 'file system mock' exists`()
            `given 'operating system mock' exists`()
            `given 'user notifier mock' exists`()
            `given 'project repository mock' sequentially returns current project`("any/path/to/project")
            `given 'basic config service' exists`()
            `given exists 'real model'`()

            variants("file state") {

                variant("file already exists") {
                    `given exists 'action preferences'`("report error")
                    `given 'file system mock' returns that folder`("exists")
                    `given 'file system mock' returns that file`("exists")
                    `when model executes command 'open file in current project'`(ProjectFile.File0)
                    `then 'file system mock' checks folder path exist'`()
                    `then 'file system mock' checks file path exist'`()
                    `then 'operating system mock' opens file`()
                    `then model returns`("success")
                }

                variant("file missing") {

                    variants("user preference if no file") {

                        variant("report error") {
                            `given exists 'action preferences'`("report error")
                            `given 'file system mock' returns that folder`("exists")
                            `given 'file system mock' returns that file`("doesn't exist")
                            `when model executes command 'open file in current project'`(ProjectFile.File0)
                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then model returns`("error: no file exists")
                        }

                        variant("notify user") {

                            `given exists 'action preferences'`("notify user")

                            variants("user actions after notification") {

                                variant("user creates file") {
                                    `given 'file system mock' returns that folder`("exists")
                                    `given 'file system mock' sequentially returns that file`("doesn't exist", "exists")
                                    `when model executes command 'open file in current project'`(ProjectFile.File0)
                                    `then 'file system mock' checks folder path exist'`()
                                    `then model notify user to`("create file")
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'operating system mock' opens file`()
                                    `then model returns`("success")
                                }

                                variant("user doesn't create file") {
                                    `given 'file system mock' returns that folder`("exists")
                                    `given 'file system mock' sequentially returns that file`(
                                        "doesn't exist",
                                        "doesn't exist"
                                    )
                                    `when model executes command 'open file in current project'`(ProjectFile.File0)
                                    `then 'file system mock' checks folder path exist'`()
                                    `then model notify user to`("create file")
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then model returns`("error: no file exists")
                                }
                            }
                        }

                        variant("auto create") {
                            `given exists 'action preferences'`("auto create")
                            `given 'file system mock' returns that folder`("exists")
                            `given 'file system mock' sequentially returns that file`("doesn't exist", "exists")
                            `given 'file system mock' returns that file is created successfully`()
                            `when model executes command 'open file in current project'`(ProjectFile.File0)
                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'file system mock' creates file`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`()
                            `then model returns`("success")
                        }
                    }
                }
            }
        }
    }
}
