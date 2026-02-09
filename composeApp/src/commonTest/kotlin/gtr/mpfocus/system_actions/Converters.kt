package gtr.mpfocus.system_actions

object Converters {

    fun String.exists(): Boolean {
        return when (this) {
            "exists" -> true
            "doesn't exist" -> false
            else -> throw IllegalArgumentException("unknown option: $this")
        }
    }
}