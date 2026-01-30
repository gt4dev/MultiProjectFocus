package gtr.hotest

import gtr.hotest.variants.VariantsRuntime

// `fun hotest` and `fun hotestAsync` differ ONLY by the `suspend` keyword.
// WHEN MODIFYING ANY OF THESE FUNCTIONS, MAKE SURE BOTH ARE UPDATED!
// BTW. eliminating this duplication would make the code messier than the duplication itself.

fun hotest(
    hotestCtx: HOTestCtx = HOTestCtx(),
    testBody: HOTestCtx.() -> Unit
): HOTestCtx {
    val runtime = VariantsRuntime()
    val previousRuntime = hotestCtx.variantsRuntime
    hotestCtx.variantsRuntime = runtime
    runtime.resetForHotest()

    try {
        while (runtime.hasPendingRuns()) {
            val selection = runtime.nextSelection()
            runtime.startRun(selection)
            hotestCtx.testBody()
        }
        return hotestCtx
    } finally {
        hotestCtx.variantsRuntime = previousRuntime
    }
}

suspend fun hotestAsync(
    hotestCtx: HOTestCtx = HOTestCtx(),
    testBody: suspend HOTestCtx.() -> Unit
): HOTestCtx {
    val runtime = VariantsRuntime()
    val previousRuntime = hotestCtx.variantsRuntime
    hotestCtx.variantsRuntime = runtime
    runtime.resetForHotest()

    try {
        while (runtime.hasPendingRuns()) {
            val selection = runtime.nextSelection()
            runtime.startRun(selection)
            hotestCtx.testBody()
        }
        return hotestCtx
    } finally {
        hotestCtx.variantsRuntime = previousRuntime
    }
}
