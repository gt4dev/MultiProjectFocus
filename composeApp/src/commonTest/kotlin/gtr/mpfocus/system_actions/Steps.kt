package gtr.mpfocus.system_actions

import gtr.hotest.HOTestCtx

object Steps {

    private const val KEY_FILE_SYSTEM_ACTIONS = "FakeFileSystemActions"

    fun HOTestCtx.`given 'fake file system' claims folder doesn't exist`() {
        val ffsa = this.getFakeFileSystemActions()
        ffsa.returnPathExists = false
    }

    fun HOTestCtx.`given 'fake file system' claims folder was created successfully`() {
        val ffsa = this.getFakeFileSystemActions()
        ffsa.returnPathCreated = false
    }

    private fun HOTestCtx.getFakeFileSystemActions(): FakeFileSystemActions {
        if (this.containsKey(KEY_FILE_SYSTEM_ACTIONS)) {
            return this[KEY_FILE_SYSTEM_ACTIONS]
        }

        val ffs = FakeFileSystemActions()
        this[KEY_FILE_SYSTEM_ACTIONS] = ffs
        return ffs
    }

}

