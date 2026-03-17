package gtr.mpfocus.domain.model.core

import dev.hotest.Async.hotest
import dev.hotest.HOTestCtx
import dev.hotest.variants.Async.variant
import dev.hotest.variants.Async.variants
import gtr.mpfocus.domain.model.config.ConfigServiceSteps.`given 'basic config service' exists`
import gtr.mpfocus.domain.model.core.CoreActionsSteps.`then model returns`
import gtr.mpfocus.domain.model.core.Models.Project
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'caller notification' response is set`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'project action preferences' is set`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'real project actions' exists`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`then 'caller notification' receives`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open file in current project'`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open file in pinned project'`
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`when 'project actions' executes command 'open file in regular project'`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns other projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns pinned projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' sequentially returns current project`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' returns that folder`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' sequentially returns that file`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'operating system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' checks file path exist'`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' checks folder path exist'`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'operating system mock' opens file`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class OpenFileTest {

    @Test
    fun `open project file - happy day scenario`() = runTest {

        hotest {
            `given 'file system mock' returns that folder`("exists")
            `given 'file system mock' returns that file`("exists")
            `given 'operating system mock' exists`()
            `given 'basic config service' exists`()
            `given 'real project actions' exists`()

            `given 'project repository mock' returns sample projects - current, pinned, regular`()

            variants("open file in project of type:") {

                variant("current project") {

                    variants("open once File1, once File5") {

                        variant("File1") {
                            `when 'project actions' executes command 'open file in current project'`(
                                ProjectFile.File1
                            )

                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`("path/to/current-project/main.md")
                            `then model returns`("success")
                        }

                        variant("File5") {
                            `when 'project actions' executes command 'open file in current project'`(
                                ProjectFile.File5
                            )

                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`("path/to/current-project/file5.md")
                            `then model returns`("success")
                        }
                    }

                }

                variant("pinned project") {

                    variants("open once File1, once File5") {

                        variant("File1") {
                            `when 'project actions' executes command 'open file in pinned project'`(
                                pinPosition = 3,
                                file = ProjectFile.File1
                            )

                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`("any/path/project333/main.md")
                            `then model returns`("success")
                        }

                        variant("File5") {
                            `when 'project actions' executes command 'open file in pinned project'`(
                                pinPosition = 3,
                                file = ProjectFile.File5
                            )

                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`("any/path/project333/file5.md")
                            `then model returns`("success")
                        }
                    }

                }

                variant("other project") {

                    variants("open once File1, once File5") {

                        variant("File1") {
                            `when 'project actions' executes command 'open file in regular project'`(
                                projectId = 555,
                                file = ProjectFile.File1
                            )

                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`("any/path/project555/main.md")
                            `then model returns`("success")
                        }

                        variant("File5") {
                            `when 'project actions' executes command 'open file in regular project'`(
                                projectId = 555,
                                file = ProjectFile.File5
                            )

                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then 'operating system mock' opens file`("any/path/project555/file5.md")
                            `then model returns`("success")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `open current project file - but no 'current project' is set`() = runTest {

        hotest {
            `given 'file system mock' returns that folder`("exists")
            `given 'file system mock' returns that file`("exists")
            `given 'operating system mock' exists`()
            `given 'basic config service' exists`()
            `given 'real project actions' exists`()

            variants("preference 'if no current project' = ..") {

                variant(".. return error") {
                    `given 'project action preferences' is set`(
                        ifNoCurrentProject = "return error"
                    )
                    `given 'project repository mock' returns current project`(null)

                    `when 'project actions' executes command 'open file in current project'`(
                        ProjectFile.File1
                    )

                    `then model returns`("error: no current project")
                }

                variant(".. notify caller") {
                    `given 'project action preferences' is set`(
                        ifNoCurrentProject = "notify caller"
                    )

                    variants("after notification, caller decision is ..") {

                        variant(".. cancel") {
                            `given 'caller notification' response is set`(
                                noCurrentProjectResponse = "cancel"
                            )
                            `given 'project repository mock' returns current project`(null)

                            `when 'project actions' executes command 'open file in current project'`(
                                ProjectFile.File1
                            )

                            `then 'caller notification' receives`("no current project")
                            `then model returns`("error: no current project")
                        }

                        variant(".. continue") {
                            `given 'caller notification' response is set`(
                                noCurrentProjectResponse = "continue"
                            )

                            variants("let's check now status of 'current project' ..") {

                                variant(".. still not set") {
                                    `given 'project repository mock' sequentially returns current project`(
                                        null,
                                        null,
                                    )

                                    `when 'project actions' executes command 'open file in current project'`(
                                        ProjectFile.File1
                                    )

                                    `then 'caller notification' receives`("no current project")
                                    `then model returns`("error: no current project")
                                }

                                variant(".. already set") {
                                    `given 'project repository mock' sequentially returns current project`(
                                        null,
                                        "path/to/current-project/123",
                                    )

                                    `when 'project actions' executes command 'open file in current project'`(
                                        ProjectFile.File1
                                    )

                                    `then 'caller notification' receives`("no current project")
                                    `then 'file system mock' checks folder path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'operating system mock' opens file`("path/to/current-project/123/main.md")
                                    `then model returns`("success")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `open project file - but project file doesn't exist`() = runTest {

        hotest {
            `given 'file system mock' returns that folder`("exists")
            `given 'operating system mock' exists`()
            `given 'basic config service' exists`()
            `given 'real project actions' exists`()

            `given 'project repository mock' returns sample projects - current, pinned, regular`()

            variants("preference 'if no file or folder' = ..") {

                variant(".. return error") {

                    `given 'project action preferences' is set`(
                        ifNoFileOrFolder = "return error"
                    )
                    `given 'file system mock' returns that file`("doesn't exist")

                    `for each project variant - current, pinned, regular - 'project actions' executes command 'open file in current project'`()

                    `then 'file system mock' checks folder path exist'`()
                    `then 'file system mock' checks file path exist'`()
                    `then model returns`("error: no file exists")
                }

                variant(".. notify caller") {

                    `given 'project action preferences' is set`(
                        ifNoFileOrFolder = "notify caller"
                    )

                    variants("after notifying, caller decision is ..") {

                        variant(".. cancel") {
                            `given 'caller notification' response is set`(
                                noFileResponse = "cancel"
                            )
                            `given 'file system mock' returns that file`("doesn't exist")

                            `for each project variant - current, pinned, regular - 'project actions' executes command 'open file in current project'`()

                            `then 'caller notification' receives`("no file")
                            `then 'file system mock' checks folder path exist'`()
                            `then 'file system mock' checks file path exist'`()
                            `then model returns`("error: no file exists")
                        }

                        variant(".. continue") {
                            `given 'caller notification' response is set`(
                                noFileResponse = "continue"
                            )

                            variants("now check again status of 'file' ..") {

                                variant(".. still doesn't exist") {
                                    `given 'file system mock' sequentially returns that file`(
                                        "doesn't exist",
                                        "doesn't exist",
                                    )

                                    `for each project variant - current, pinned, regular - 'project actions' executes command 'open file in current project'`()

                                    `then 'caller notification' receives`("no file")
                                    `then 'file system mock' checks folder path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then model returns`("error: no file exists")
                                }

                                variant(".. already exists") {
                                    `given 'file system mock' sequentially returns that file`(
                                        "doesn't exist",
                                        "exists",
                                    )

                                    `when 'project actions' executes command 'open file in pinned project'`(
                                        pinPosition = 3,
                                        file = ProjectFile.File1
                                    )

                                    `then 'caller notification' receives`("no file")
                                    `then 'file system mock' checks folder path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'file system mock' checks file path exist'`()
                                    `then 'operating system mock' opens file`("any/path/project333/main.md")
                                    `then model returns`("success")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun HOTestCtx.`for each project variant - current, pinned, regular - 'project actions' executes command 'open file in current project'`() {
    variants("test for each project type: ..") {

        variant(".. current project") {
            `when 'project actions' executes command 'open file in current project'`(
                ProjectFile.File1
            )
        }

        variant(".. pinned project") {
            `when 'project actions' executes command 'open file in pinned project'`(
                pinPosition = 3,
                file = ProjectFile.File1
            )
        }

        variant(".. regular project") {
            `when 'project actions' executes command 'open file in regular project'`(
                projectId = 555,
                file = ProjectFile.File1
            )
        }
    }
}

suspend fun HOTestCtx.`given 'project repository mock' returns sample projects - current, pinned, regular`() {
    `given 'project repository mock' returns current project`(
        Project(path = "path/to/current-project")
    )

    `given 'project repository mock' returns pinned projects`(
        Project(id = 111, path = "any/path/project111", pinPosition = 1),
        Project(id = 222, path = "any/path/project222", pinPosition = 2),
        Project(id = 333, path = "any/path/project333", pinPosition = 3),
    )

    `given 'project repository mock' returns other projects`(
        Project(id = 444, path = "any/path/project444"),
        Project(id = 555, path = "any/path/project555"),
        Project(id = 666, path = "any/path/project666"),
        Project(id = 777, path = "any/path/project777"),
        Project(id = 888, path = "any/path/project888"),
    )
}