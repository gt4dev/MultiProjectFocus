package gtr.mpfocus.system_actions

import gtr.hotest.HOTestCtx

object Steps {

    const val KEY_FILE_SYSTEM_ACTIONS = "KEY_FILE_SYSTEM_ACTIONS"
    const val KEY_OPERATING_SYSTEM_ACTIONS = "KEY_OPERATING_SYSTEM_ACTIONS"

    fun HOTestCtx.`given 'fake file system' always returns that folder doesn't exist`() {
        val ffsa = this.initFakeFileSystemActions()
        ffsa.returnPathExists = false
    }

    fun HOTestCtx.`given 'fake file system' always returns that folder is created successfully`() {
        val ffsa = this.initFakeFileSystemActions()
        ffsa.returnPathCreated = true
    }

    fun HOTestCtx.`then 'fake file system' checks path exist'`() {
    }

    fun HOTestCtx.`then 'fake file system' creates folder`() {
    }

    fun HOTestCtx.`given exists 'fake operating system'`() {
        this.initFakeOperatingSystemActions()
    }

    fun HOTestCtx.`then 'fake operating system' opens folder`() {
    }

    private fun HOTestCtx.initFakeFileSystemActions(): FakeFileSystemActions {
        if (this.containsKey(KEY_FILE_SYSTEM_ACTIONS)) {
            return this[KEY_FILE_SYSTEM_ACTIONS]
        }

        val ffs = FakeFileSystemActions()
        this[KEY_FILE_SYSTEM_ACTIONS] = ffs
        return ffs
    }

    private fun HOTestCtx.initFakeOperatingSystemActions(): FakeOperatingSystemActions {
        if (this.containsKey(KEY_OPERATING_SYSTEM_ACTIONS)) {
            return this[KEY_OPERATING_SYSTEM_ACTIONS]
        }

        val fos = FakeOperatingSystemActions()
        this[KEY_OPERATING_SYSTEM_ACTIONS] = fos
        return fos
    }
}

