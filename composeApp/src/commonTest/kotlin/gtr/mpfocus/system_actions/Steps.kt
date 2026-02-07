package gtr.mpfocus.system_actions

import dev.mokkery.*
import dev.mokkery.answering.returns
import dev.mokkery.matcher.any
import gtr.hotest.HOTestCtx

object Steps {

    const val KEY_FILE_SYSTEM_ACTIONS = "KEY_FILE_SYSTEM_ACTIONS"
    const val KEY_OPERATING_SYSTEM_ACTIONS = "KEY_OPERATING_SYSTEM_ACTIONS"

    fun HOTestCtx.`given 'fake file system' returns that folder doesn't exist`() {
        val obj = this.initMockFileSystemActions()
        every { obj.pathExists(any()) } returns false
    }

    fun HOTestCtx.`given 'fake file system' returns that folder is created successfully`() {
        val obj = this.initMockFileSystemActions()
        every { obj.createFolder(any()) } returns true
    }

    fun HOTestCtx.`then 'fake file system' checks path exist'`() {
        val obj: FileSystemActions = this[KEY_FILE_SYSTEM_ACTIONS]
        verify {
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

