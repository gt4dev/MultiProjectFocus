package gtr.hotest

fun hotest(
    hotestCtx: HOTestCtx = HOTestCtx(),
    testBody: HOTestCtx.() -> Unit
): HOTestCtx {
    hotestCtx.testBody()
    return hotestCtx
}


suspend fun hotestAsync(
    hotestCtx: HOTestCtx = HOTestCtx(),
    testBody: suspend HOTestCtx.() -> Unit
): HOTestCtx {
    hotestCtx.testBody()
    return hotestCtx
}