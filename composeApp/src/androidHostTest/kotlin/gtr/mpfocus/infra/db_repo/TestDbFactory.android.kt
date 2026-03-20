package gtr.mpfocus.infra.db_repo

import org.junit.Assume.assumeTrue

internal actual fun createInMemoryTestDb(): MPFDatabase {
    assumeTrue(
        "Room on Android requires instrumentation. TODO when needed.",
        false
    )
    error("Unreachable code")
}
