package gtr.hotest.variants

internal class VariantsRuntime {

    private val pendingRuns = mutableListOf<MutableMap<Int, Int>>()
    internal var currentSelection: MutableMap<Int, Int> = mutableMapOf()
        private set
    internal var blockIndexCounter: Int = 0
        private set
    private val blockStack = mutableListOf<BlockContext>()

    internal fun resetForHotest() {
        pendingRuns.clear()
        pendingRuns.add(mutableMapOf())
    }

    internal fun hasPendingRuns(): Boolean = pendingRuns.isNotEmpty()

    internal fun nextSelection(): MutableMap<Int, Int> = pendingRuns.removeAt(0)

    internal fun startRun(selection: MutableMap<Int, Int>) {
        currentSelection = selection
        blockIndexCounter = 0
    }

    internal fun nextBlockIndex(): Int = blockIndexCounter++

    internal fun enterBlock(blockIndex: Int): BlockContext {
        val selected = currentSelection[blockIndex]
        val ctx = BlockContext(
            blockIndex = blockIndex,
            chosenIndex = selected ?: 0,
            selectionExplicit = selected != null
        )
        blockStack.add(ctx)
        return ctx
    }

    internal fun currentBlock(): BlockContext? = blockStack.lastOrNull()

    internal fun exitBlock(): BlockContext = blockStack.removeAt(blockStack.lastIndex)

    internal fun finishBlock(ctx: BlockContext) {
        if (ctx.variantCount == 0) {
            return
        }

        if (ctx.selectionExplicit) {
            if (ctx.chosenIndex >= ctx.variantCount) {
                throw IllegalStateException(
                    "variants block ${ctx.blockIndex} has ${ctx.variantCount} options but run requested ${ctx.chosenIndex}"
                )
            }
            return
        }

        // Mark the implicit choice so sibling selections inherit it and don't re-expand this block.
        currentSelection[ctx.blockIndex] = ctx.chosenIndex
        for (i in 1 until ctx.variantCount) {
            val newSelection = currentSelection.toMutableMap()
            newSelection[ctx.blockIndex] = i
            pendingRuns.add(newSelection)
        }
    }
}

internal class BlockContext(
    val blockIndex: Int,
    val chosenIndex: Int,
    val selectionExplicit: Boolean,
    var variantCount: Int = 0
)
