package gtr.mpfocus.system_actions

import dev.hotest.HOTestCtx
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import gtr.mpfocus.system_actions.Converters.exists
import okio.Path.Companion.toPath

object FileSystemActionsSteps {

    fun HOTestCtx.`given 'file system mock' exists`() {
        initFileSystemActionsMock()
    }

    // folder steps

    fun HOTestCtx.`given 'file system mock' returns that folder`(
        status: String,
    ) {
        val obj: FileSystemActions = initFileSystemActionsMock()
        every { obj.pathExists(any<FolderPath>()) } returns status.exists()
    }

    fun HOTestCtx.`given 'file system mock' sequentially returns that folder`(
        vararg statuses: String,
    ) {
        val obj: FileSystemActions = initFileSystemActionsMock()
        val returnsVals = statuses.map { it.exists() }
        every { obj.pathExists(any<FolderPath>()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'file system mock' returns that folder is created successfully`() {
        val obj: FileSystemActions = initFileSystemActionsMock()
        every { obj.createFolder(any()) } returns true
    }

    fun HOTestCtx.`then 'file system mock' checks folder path exist'`() {
        val obj: FileSystemActions = koin.get()
        verify(mode = VerifyMode.order) { // each 'verify' counterparts each call
            obj.pathExists(any<FolderPath>())
        }
    }

    fun HOTestCtx.`then 'file system mock' creates folder`(path: String? = null) {
        val obj: FileSystemActions = koin.get()
        if (path == null) {
            verify {
                obj.createFolder(any())
            }
        } else {
            verify {
                obj.createFolder(FolderPath(path.toPath()))
            }
        }
    }

    // file steps

    fun HOTestCtx.`given 'file system mock' returns that file`(
        status: String,
    ) {
        val obj: FileSystemActions = initFileSystemActionsMock()
        every { obj.pathExists(any<FilePath>()) } returns status.exists()
    }

    fun HOTestCtx.`given 'file system mock' sequentially returns that file`(
        vararg statuses: String,
    ) {
        val obj: FileSystemActions = initFileSystemActionsMock()
        val returnsVals = statuses.map { it.exists() }
        every { obj.pathExists(any<FilePath>()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'file system mock' returns that file is created successfully`() {
        val obj: FileSystemActions = initFileSystemActionsMock()
        every { obj.createFile(any()) } returns true
    }

    fun HOTestCtx.`then 'file system mock' checks file path exist'`() {
        val obj: FileSystemActions = koin.get()
        verify(mode = VerifyMode.order) {
            obj.pathExists(any<FilePath>())
        }
    }

    fun HOTestCtx.`then 'file system mock' creates file`() {
        val obj: FileSystemActions = koin.get()
        verify {
            obj.createFile(any())
        }
    }


    // operating system steps

    fun HOTestCtx.`given 'operating system mock' exists`() {
        val obj = mock<OperatingSystemActions>()
        koinAdd {
            single { obj }
        }

        everySuspend { obj.openFolder(any()) } returns Unit
        everySuspend { obj.openFile(any()) } returns Unit
    }

    fun HOTestCtx.`then 'operating system mock' opens folder`(path: String? = null) {
        val obj: OperatingSystemActions = koin.get()
        if (path == null) {
            verifySuspend {
                obj.openFolder(any())
            }
        } else {
            verifySuspend {
                obj.openFolder(FolderPath(path.toPath()))
            }
        }
    }

    fun HOTestCtx.`then 'operating system mock' opens file`(path: String? = null) {
        val obj: OperatingSystemActions = koin.get()
        if (path == null) {
            verifySuspend {
                obj.openFile(any())
            }
        } else {
            verifySuspend {
                obj.openFile(FilePath(path.toPath()))
            }
        }
    }


    private fun HOTestCtx.initFileSystemActionsMock(): FileSystemActions {
        val existing = koin.getOrNull<FileSystemActions>()
        if (existing != null) {
            return existing
        }

        val obj = mock<FileSystemActions>()
        koinAdd {
            single { obj }
        }
        return obj
    }
}

