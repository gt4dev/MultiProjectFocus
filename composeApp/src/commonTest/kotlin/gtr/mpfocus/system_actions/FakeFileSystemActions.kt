package gtr.mpfocus.system_actions

import kotlin.properties.Delegates

class FakeFileSystemActions : FileSystemActions {

    var returnPathExists by Delegates.notNull<Boolean>()
    var returnPathCreated by Delegates.notNull<Boolean>()

    override fun pathExists(path: TmpPath): Boolean {
        return returnPathExists
    }

    override fun createFolder(path: TmpPath): Boolean {
        return returnPathCreated
    }
}