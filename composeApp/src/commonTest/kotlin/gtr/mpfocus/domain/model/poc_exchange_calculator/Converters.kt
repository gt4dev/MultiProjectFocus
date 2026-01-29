package gtr.mpfocus.domain.model.poc_exchange_calculator

object Converters {

    fun Models.ExchangeRate.toProduction(): ExchangeRate {
        return ExchangeRate(
            from = Currency.valueOf(this.from),
            to = Currency.valueOf(this.to),
            rate = this.rate
        )
    }
}