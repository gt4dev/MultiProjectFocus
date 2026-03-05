package gtr.mpfocus.domain.model.core

object Models {

    data class Project(
        val id: Long? = null,
        val path: String? = null,
        val pinPosition: Int? = null,
    )
}

/**
 * `Models` mini-guide:
 * - make all[1] properties nullable
 * - aim is to make <<<scenario>>> readable, so don't pollute scenario with unnecessary [even misleading] data
 * - thus
 *      if some properties (even if technically required [e.g. entityId])
 *         are unnecessary or easy to conclude from other areas of scenario
 *      then skip it
 * [1] unless you know that property is always required in all scenarios, but it's risky assumption
 */