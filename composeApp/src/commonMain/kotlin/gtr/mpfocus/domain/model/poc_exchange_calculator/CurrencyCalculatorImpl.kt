package gtr.mpfocus.domain.model.poc_exchange_calculator

internal class CurrencyCalculatorImpl(
    private val ratesProvider: RatesProvider
) : CurrencyCalculator {

    override fun calcExchange(
        money: Money,
        to: Currency
    ): Money {

        if (money.currency == to) {
            return money
        }

        val rates = ratesProvider.getRates()

        val directRate = rates.firstOrNull {
            it.from == money.currency && it.to == to
        }
        if (directRate != null) {
            return Money(
                amount = money.amount * directRate.rate,
                currency = to
            )
        }

        val reverseRate = rates.firstOrNull {
            it.from == to && it.to == money.currency
        }
        if (reverseRate != null) {
            return Money(
                amount = money.amount / reverseRate.rate,
                currency = to
            )
        }

        throw RateNotFoundException(from = money.currency, to = to)
    }
}