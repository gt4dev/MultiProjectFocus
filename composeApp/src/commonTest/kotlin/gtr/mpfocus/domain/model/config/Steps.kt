package gtr.mpfocus.domain.model.config

import gtr.hotest.HOTestCtx

object Steps {

    const val KEY_CONFIG_SERVICE = "KEY_CONFIG_SERVICE"

    fun HOTestCtx.`given exists 'basic config service'`() {
        this[KEY_CONFIG_SERVICE] = ConfigService.Basic
    }
}
