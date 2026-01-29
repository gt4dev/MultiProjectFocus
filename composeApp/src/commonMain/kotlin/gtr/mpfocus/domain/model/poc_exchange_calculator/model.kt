package gtr.mpfocus.domain.model.poc_exchange_calculator


enum class Currency {
    USD,
    EUR,
    GBP,
    CHF,
    PLN,
}

data class Money(
    val amount: Long,
    val currency: Currency,
)

data class ExchangeRate(
    val from: Currency,
    val to: Currency,
    val rate: Long
)


interface CurrencyCalculator {
    fun calcExchange(
        money: Money,
        to: Currency,
    ): Money
}

interface RatesProvider {
    fun getRates(): List<ExchangeRate>
}


class RateNotFoundException(
    from: Currency,
    to: Currency
) : RuntimeException()