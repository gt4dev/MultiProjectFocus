package gtr.mpfocus.domain.model.config

import gtr.hotest.HOTestCtx

object Steps {

    fun HOTestCtx.`given exists 'basic config service'`() {
        koinAdd {
            single<ConfigService> { ConfigService.Basic }
        }
    }
}
