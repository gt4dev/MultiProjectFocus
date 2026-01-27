package gtr.mpfocus.mpfocus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform