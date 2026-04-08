package gtr.mpfocus.hotest

import dev.hotest.HOTestCtx


// todo: move to HOTest project


inline fun <reified T : Any> HOTestCtx.koinAddObject(obj: T) {
    koinAdd {
        single { obj }
    }
}

inline fun <reified T : Any> HOTestCtx.koinAddIfMissing(create: () -> T): T {
    val existing = koin.getOrNull<T>()
    if (existing != null) {
        return existing
    }

    val obj = create()
    koinAddObject<T>(obj)
    return obj
}
