package gtr.hotest.variants

import gtr.hotest.HOTestCtx

fun HOTestCtx.variants(
    comment: String = "",
    testBody: HOTestCtx.() -> Unit
) {
    val runtime = this.variantsRuntime
    if (runtime == null) {
        this.testBody()
        return
    }

    val blockIndex = runtime.nextBlockIndex()
    runtime.enterBlock(blockIndex)
    try {
        this.testBody()
    } finally {
        val finished = runtime.exitBlock()
        runtime.finishBlock(finished)
    }
}

fun HOTestCtx.variant(
    comment: String = "",
    testBody: HOTestCtx.() -> Unit
) {
    val runtime = this.variantsRuntime
    val ctx = runtime?.currentBlock()
    if (ctx == null) {
        this.testBody()
        return
    }

    val index = ctx.variantCount
    ctx.variantCount += 1
    if (index == ctx.chosenIndex) {
        this.testBody()
    }
}
