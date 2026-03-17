package gtr.mpfocus.hotest

import dev.hotest.HOTestCtx


// todo: move to HOTest project


inline fun <reified T : Any> HOTestCtx.koinAddObject(obj: T) {
    koinAdd {
        single { obj }
    }
}
