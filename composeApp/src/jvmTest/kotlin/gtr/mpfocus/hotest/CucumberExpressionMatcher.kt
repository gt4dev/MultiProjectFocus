package gtr.mpfocus.hotest

// todo: 1/ move to commonTest, then to hotest lib [+ add tests] or 2/ best use original CucumberExpression matcher [after translating it to KMP]

class CucumberExpressionMatcher(private val input: String) {
    private var lastValues: List<Any> = emptyList()
    private val compiledCache = mutableMapOf<String, CompiledPattern>()

    fun matches(expression: String): Boolean {
        val compiled = compiledCache.getOrPut(expression) { compile(expression) }
        val match = compiled.regex.matchEntire(input)
            ?: return false.also { lastValues = emptyList() }

        val groups = match.groupValues.drop(1)
        lastValues = compiled.converters.zip(groups).map { (converter, raw) -> converter(raw) }
        return true
    }

    fun getInt(index: Int): Int = get(index, Int::class)

    fun getLong(index: Int): Long = get(index, Long::class)

    fun getString(index: Int): String = get(index, String::class)

    private fun <T : Any> get(index: Int, expected: kotlin.reflect.KClass<T>): T {
        require(index in lastValues.indices) {
            "No argument at index $index. Available: ${lastValues.size}."
        }
        val value = lastValues[index]
        require(expected.isInstance(value)) {
            "Argument at index $index is ${value::class.simpleName}, expected ${expected.simpleName}."
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

    private fun compile(expression: String): CompiledPattern {
        val regex = StringBuilder("^")
        val converters = mutableListOf<(String) -> Any>()

        var i = 0
        while (i < expression.length) {
            if (expression[i] == '{') {
                val end = expression.indexOf('}', i + 1)
                require(end != -1) { "Unclosed parameter in expression: $expression" }

                val parameterType = expression.substring(i + 1, end).trim()
                val token = tokenFor(parameterType)
                regex.append('(').append(token.regex).append(')')
                converters.add(token.converter)
                i = end + 1
                continue
            }

            regex.append(Regex.escape(expression[i].toString()))
            i++
        }

        regex.append("$")
        return CompiledPattern(Regex(regex.toString()), converters)
    }

    private fun tokenFor(parameterType: String): ParameterToken {
        return when (parameterType) {
            "int" -> ParameterToken("-?\\d+") { it.toInt() }
            "long" -> ParameterToken("-?\\d+") { it.toLong() }
            "word" -> ParameterToken("\\S+") { it }
            "string" -> ParameterToken(".+") { normalizeString(it) }
            else -> error("Unsupported parameter type '{$parameterType}'.")
        }
    }

    private fun normalizeString(raw: String): String {
        if (raw.length >= 2) {
            val first = raw.first()
            val last = raw.last()
            if ((first == '\'' && last == '\'') || (first == '"' && last == '"')) {
                return raw.substring(1, raw.length - 1)
            }
        }
        return raw
    }

    private data class CompiledPattern(
        val regex: Regex,
        val converters: List<(String) -> Any>
    )

    private data class ParameterToken(
        val regex: String,
        val converter: (String) -> Any
    )
}
