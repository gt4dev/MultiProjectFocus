package gtr.mpfocus.domain.model.poc_exchange_calculator

import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.poc_exchange_calculator.Converters.toProduction
import kotlin.test.assertTrue

object Steps {

    private const val KEY_RATES_PROVIDER = "RatesProvider"
    private const val KEY_CALCULATOR = "CurrencyCalculator"
    private const val KEY_RESULTS = "ExchangeResults"

    fun HOTestCtx.`given there is a fake 'exchange rates' service`() {
        this[KEY_RATES_PROVIDER] = FakeRatesProvider()
    }

    fun HOTestCtx.`given 'exchange rates' returns rates`(vararg rates: Models.ExchangeRate) {
        val frp: FakeRatesProvider = this[KEY_RATES_PROVIDER]
        frp.fakeRates = rates.map {
            it.toProduction()
        }
    }

    fun HOTestCtx.`given there is real exchange calculator`() {
        val ratesProvider: RatesProvider = this[KEY_RATES_PROVIDER]
        this[KEY_CALCULATOR] = CurrencyCalculatorImpl(ratesProvider)
    }

    fun HOTestCtx.`when exchange calculator converts`(
        money: Models.Money,
        to: Currency
    ) {
        val calculator: CurrencyCalculator = this[KEY_CALCULATOR]

        val result = try {
            calculator.calcExchange(
                Money(
                    money.amount,
                    Currency.valueOf(money.currency)
                ),
                to = to,
            )
        } catch (e: Exception) {
            e
        }

        this[KEY_RESULTS] = result
    }


    fun HOTestCtx.`then exchange calculator returns`(
        money: Models.Money,
    ) {
        val result: Money = this[KEY_RESULTS]
        Assertions.moneyEquals(money, result)
    }

    fun HOTestCtx.`then exchange calculator reports no rate error`() {
        val result: Any = this[KEY_RESULTS]

        assertTrue(result is RateNotFoundException)
    }
}