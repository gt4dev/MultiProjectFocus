package gtr.mpfocus

import gtr.mpfocus.domain.model.platform.MPFile
import gtr.mpfocus.domain.model.platform.MPFolder
import gtr.mpfocus.domain.model.platform.OperatingSystemActions
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