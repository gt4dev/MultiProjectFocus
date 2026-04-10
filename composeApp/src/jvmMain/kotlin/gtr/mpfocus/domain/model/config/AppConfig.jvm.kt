package gtr.mpfocus.domain.model.config

actual fun getUserHomePath(): String = System.getProperty("user.home")
