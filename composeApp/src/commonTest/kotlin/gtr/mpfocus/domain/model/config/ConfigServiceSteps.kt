package gtr.mpfocus.domain.model.config

import dev.hotest.HOTestCtx

object ConfigServiceSteps {

    fun HOTestCtx.`given 'basic config service' exists`() {
        koinAdd {
            single<ConfigService> { ConfigService.Basic }
        }
    }
}
