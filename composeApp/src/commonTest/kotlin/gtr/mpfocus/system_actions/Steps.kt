package gtr.mpfocus.system_actions

import dev.mokkery.*
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.matcher.any
import dev.mokkery.verify.VerifyMode
import gtr.hotest.HOTestCtx
import gtr.mpfocus.system_actions.Converters.exists

object Steps {

    const val KEY_FILE_SYSTEM_ACTIONS = "KEY_FILE_SYSTEM_ACTIONS"
    const val KEY_OPERATING_SYSTEM_ACTIONS = "KEY_OPERATING_SYSTEM_ACTIONS"

    fun HOTestCtx.`given exists 'fake file system'`() {
        this.initMockFileSystemActions()
    }

    fun HOTestCtx.`given 'fake file system' returns that folder`(state: String) {
        val obj = this.initMockFileSystemActions()
        every { obj.pathExists(any()) } returns state.exists()
    }

    fun HOTestCtx.`given 'fake file system' returns that folder`(
        vararg subsequentReturns: String,
    ) {
        val returnsVals = subsequentReturns.map { it.exists() }
        val obj = this.initMockFileSystemActions()
        every { obj.pathExists(any()) } sequentiallyReturns returnsVals
    }

    fun HOTestCtx.`given 'fake file system' returns that folder is created successfully`() {
        val obj = this.initMockFileSystemActions()
        every { obj.createFolder(any()) } returns true
    }

    fun HOTestCtx.`then 'fake file system' checks path exist'`() {
        val obj: FileSystemActions = this[KEY_FILE_SYSTEM_ACTIONS]
        verify(mode = VerifyMode.order) { // each 'verify' counterparts each call
            obj.pathExists(any())
        }
    }

    fun HOTestCtx.`then 'fake file system' creates folder`() {
        val obj: FileSystemActions = this[KEY_FILE_SYSTEM_ACTIONS]
        verify {
            obj.createFolder(any())
        }
    }

    fun HOTestCtx.`given exists 'fake operating system'`() {
        val obj = this.initMockOperatingSystemActions()
        everySuspend { obj.openFolder(any()) } returns Unit
        everySuspend { obj.openFile(any()) } returns Unit
    }

    fun HOTestCtx.`then 'fake operating system' opens folder`() {
        val obj: OperatingSystemActions = this[KEY_OPERATING_SYSTEM_ACTIONS]
        verifySuspend {
            obj.openFolder(any())
        }
    }

    private fun HOTestCtx.initMockFileSystemActions(): FileSystemActions {
        if (this.containsKey(KEY_FILE_SYSTEM_ACTIONS)) {
            return this[KEY_FILE_SYSTEM_ACTIONS]
        }

        val obj = mock<FileSystemActions>()
        this[KEY_FILE_SYSTEM_ACTIONS] = obj
        return obj
    }

    private fun HOTestCtx.initMockOperatingSystemActions(): OperatingSystemActions {
        if (this.containsKey(KEY_OPERATING_SYSTEM_ACTIONS)) {
            return this[KEY_OPERATING_SYSTEM_ACTIONS]
        }

        val obj = mock<OperatingSystemActions>()
        this[KEY_OPERATING_SYSTEM_ACTIONS] = obj
        return obj
    }
}

