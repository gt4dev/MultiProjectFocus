package gtr.mpfocus

import gtr.mpfocus.os_actions.MPFile
import gtr.mpfocus.os_actions.MPFolder
import gtr.mpfocus.os_actions.OperatingSystemActions
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val m = OperatingSystemActions()
    m.openFile(MPFile("""C:\Users\gtr\Dropbox\wf67\projects\home\px\aps\test123.md"""))
    m.openFolder(MPFolder("""C:\Users\gtr\Dropbox\wf67\projects\home\px\"""))
}

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "mpfocus",
//    ) {
//        App()
//    }
//}