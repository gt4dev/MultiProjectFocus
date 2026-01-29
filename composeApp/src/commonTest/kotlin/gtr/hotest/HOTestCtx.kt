package gtr.hotest

class HOTestCtx {

    val hotestCtx: HOTestCtx = this

    private val items = mutableMapOf<String, Any>()

    operator fun <T> set(key: String, value: T) {
        if (items.containsKey(key)) {
            // forbid "map" modification - it simplifies code
            throw IllegalArgumentException("key $key already exists")
        }
        items[key] = value as Any
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String): T {
        if (!items.containsKey(key)) {
            throw IllegalArgumentException("key $key doesn't exist")
        }
        return items[key] as T
    }
}

fun hotestCtx(test: HOTestCtx.() -> Unit) {
    val ctx = HOTestCtx()
    ctx.test()
}

object Suspendable {
    suspend fun hotestCtx(test: suspend HOTestCtx.() -> Unit) {
        val ctx = HOTestCtx()
        ctx.test()
    }
}