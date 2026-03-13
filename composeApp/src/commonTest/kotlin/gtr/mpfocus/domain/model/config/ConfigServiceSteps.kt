package gtr.mpfocus.domain.model.config

import gtr.hotest.HOTestCtx

object ConfigServiceSteps {

    fun HOTestCtx.`given 'basic config service' exists`() {
        koinAdd {
            single<ConfigService> { ConfigService.Basic }
        }
    }
}
