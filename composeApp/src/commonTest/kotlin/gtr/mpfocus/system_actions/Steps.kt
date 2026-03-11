package gtr.mpfocus.system_actions

import dev.mokkery.*
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.matcher.any
import dev.mokkery.verify.VerifyMode
import gtr.hotest.HOTestCtx
import gtr.mpfocus.system_actions.Converters.exists
import okio.Path.Companion.toPath

object Steps {

    // file system steps

    fun HOTestCtx.`given exists 'fake file system'`() {
        val obj = mock<FileSystemActions>()
        koinAdd {
            single { obj }
        }
    }

    // folder steps

    fun HOTestCtx.`given 'fake file system' returns that folder`(
        status: String,
    ) {
        val obj: FileSystemActions = koin.get()
        every { obj.pathExists(any<FolderPath>()) } returns status.exists()
    }

    fun HOTestCtx.`given 'fake file system' sequentially returns that folder`(
        vararg statuses: String,
    ) {
        val obj: FileSystemActions = koin.get()
        val returnsVals = statuses.map { it.exists() }
        every { obj.pathExists(any<FolderPath>()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'fake file system' returns that folder is created successfully`() {
        val obj: FileSystemActions = koin.get()
        every { obj.createFolder(any()) } returns true
    }

    fun HOTestCtx.`then 'fake file system' checks folder path exist'`() {
        val obj: FileSystemActions = koin.get()
        verify(mode = VerifyMode.order) { // each 'verify' counterparts each call
            obj.pathExists(any<FolderPath>())
        }
    }

    fun HOTestCtx.`then 'fake file system' creates folder`() {
        val obj: FileSystemActions = koin.get()
        verify {
            obj.createFolder(any())
        }
    }

    // file steps

    fun HOTestCtx.`given 'fake file system' returns that file`(
        status: String,
    ) {
        val obj: FileSystemActions = koin.get()
        every { obj.pathExists(any<FilePath>()) } returns status.exists()
    }

    fun HOTestCtx.`given 'fake file system' sequentially returns that file`(
        vararg statuses: String,
    ) {
        val obj: FileSystemActions = koin.get()
        val returnsVals = statuses.map { it.exists() }
        every { obj.pathExists(any<FilePath>()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'fake file system' returns that file is created successfully`() {
        val obj: FileSystemActions = koin.get()
        every { obj.createFile(any()) } returns true
    }

    fun HOTestCtx.`then 'fake file system' checks file path exist'`() {
        val obj: FileSystemActions = koin.get()
        verify(mode = VerifyMode.order) {
            obj.pathExists(any<FilePath>())
        }
    }

    fun HOTestCtx.`then 'fake file system' creates file`() {
        val obj: FileSystemActions = koin.get()
        verify {
            obj.createFile(any())
        }
    }


    // operating system steps

    fun HOTestCtx.`given exists 'fake operating system'`() {
        val obj = mock<OperatingSystemActions>()
        koinAdd {
            single { obj }
        }

        everySuspend { obj.openFolder(any()) } returns Unit
        everySuspend { obj.openFile(any()) } returns Unit
    }

    fun HOTestCtx.`then 'fake operating system' opens folder`(path: String? = null) {
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

    fun HOTestCtx.`then 'fake operating system' opens file`() {
        val obj: OperatingSystemActions = koin.get()
        verifySuspend {
            obj.openFile(any())
        }
    }
}

