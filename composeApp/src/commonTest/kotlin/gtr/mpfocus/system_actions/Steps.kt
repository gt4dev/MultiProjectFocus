package gtr.mpfocus.system_actions

import dev.mokkery.*
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.matcher.any
import dev.mokkery.verify.VerifyMode
import gtr.hotest.HOTestCtx
import gtr.mpfocus.system_actions.Converters.exists

object Steps {

    // file system steps

    fun HOTestCtx.`given exists 'fake file system'`() {
        val obj = mock<FileSystemActions>()
        this.addToKoinTestModule {
            single { obj }
        }
    }

    // folder steps

    fun HOTestCtx.`given 'fake file system' returns that each folder`(
        vararg subsequentReturns: String,
    ) {
        val obj: FileSystemActions = this.koin.get()
        val returnsVals = subsequentReturns.map { it.exists() }
        every { obj.pathExists(any<FolderPath>()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'fake file system' returns that folder is created successfully`() {
        val obj: FileSystemActions = this.koin.get()
        every { obj.createFolder(any()) } returns true
    }

    fun HOTestCtx.`then 'fake file system' checks folder path exist'`() {
        val obj: FileSystemActions = this.koin.get()
        verify(mode = VerifyMode.order) { // each 'verify' counterparts each call
            obj.pathExists(any<FolderPath>())
        }
    }

    fun HOTestCtx.`then 'fake file system' creates folder`() {
        val obj: FileSystemActions = this.koin.get()
        verify {
            obj.createFolder(any())
        }
    }

    // file steps

    fun HOTestCtx.`given 'fake file system' returns that each file`(
        vararg subsequentReturns: String,
    ) {
        val obj: FileSystemActions = this.koin.get()
        val returnsVals = subsequentReturns.map { it.exists() }
        every { obj.pathExists(any<FilePath>()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'fake file system' returns that file is created successfully`() {
        val obj: FileSystemActions = this.koin.get()
        every { obj.createFile(any()) } returns true
    }

    fun HOTestCtx.`then 'fake file system' checks file path exist'`() {
        val obj: FileSystemActions = this.koin.get()
        verify(mode = VerifyMode.order) {
            obj.pathExists(any<FilePath>())
        }
    }

    fun HOTestCtx.`then 'fake file system' creates file`() {
        val obj: FileSystemActions = this.koin.get()
        verify {
            obj.createFile(any())
        }
    }


    // operating system steps

    fun HOTestCtx.`given exists 'fake operating system'`() {
        val obj = mock<OperatingSystemActions>()
        this.addToKoinTestModule {
            single { obj }
        }

        everySuspend { obj.openFolder(any()) } returns Unit
        everySuspend { obj.openFile(any()) } returns Unit
    }

    fun HOTestCtx.`then 'fake operating system' opens folder`() {
        val obj: OperatingSystemActions = this.koin.get()
        verifySuspend {
            obj.openFolder(any())
        }
    }

    fun HOTestCtx.`then 'fake operating system' opens file`() {
        val obj: OperatingSystemActions = this.koin.get()
        verifySuspend {
            obj.openFile(any())
        }
    }
}

