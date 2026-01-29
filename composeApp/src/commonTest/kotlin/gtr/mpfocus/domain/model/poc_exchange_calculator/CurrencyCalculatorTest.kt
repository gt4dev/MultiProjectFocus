package gtr.mpfocus.domain.model.poc_exchange_calculator

import gtr.hotest.Suspendable.hotestCtx
import gtr.mpfocus.domain.model.poc_exchange_calculator.Models.ExchangeRate
import gtr.mpfocus.domain.model.poc_exchange_calculator.Models.Money
import gtr.mpfocus.domain.model.poc_exchange_calculator.Steps.`given 'exchange rates' returns rates`
import gtr.mpfocus.domain.model.poc_exchange_calculator.Steps.`given there is a fake 'exchange rates' service`
import gtr.mpfocus.domain.model.poc_exchange_calculator.Steps.`given there is real exchange calculator`
import gtr.mpfocus.domain.model.poc_exchange_calculator.Steps.`then exchange calculator reports no rate error`
import gtr.mpfocus.domain.model.poc_exchange_calculator.Steps.`then exchange calculator returns`
import gtr.mpfocus.domain.model.poc_exchange_calculator.Steps.`when exchange calculator converts`
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CurrencyCalculatorTest {

    @Test
    fun `exchange currencies - direct rate use`() = runTest {
        hotestCtx {
            `given there is a fake 'exchange rates' service`()
            `given 'exchange rates' returns rates`(
                ExchangeRate("EUR", "PLN", 4),
                ExchangeRate("EUR", "CHF", 2),
                ExchangeRate("EUR", "USD", 1)
            )

            `given there is real exchange calculator`()

            `when exchange calculator converts`(
                Money(10, "EUR"),
                Currency.PLN
            )
            `then exchange calculator returns`(
                Money(40, "PLN"),
            )
        }
    }

    @Test
    fun `exchange currencies - reversed rate use`() = runTest {
        hotestCtx {
            `given there is a fake 'exchange rates' service`()
            `given 'exchange rates' returns rates`(
                ExchangeRate("EUR", "PLN", 4),
                ExchangeRate("EUR", "CHF", 2),
                ExchangeRate("EUR", "USD", 1)
            )

            `given there is real exchange calculator`()

            `when exchange calculator converts`(
                Money(40, "PLN"),
                Currency.EUR
            )
            `then exchange calculator returns`(
                Money(10, "EUR"),
            )
        }
    }

    @Test
    fun `exchange currencies - the same from and to`() = runTest {
        hotestCtx {
            `given there is a fake 'exchange rates' service`()
            `given 'exchange rates' returns rates`(
                ExchangeRate("EUR", "PLN", 4),
                ExchangeRate("EUR", "CHF", 2),
                ExchangeRate("EUR", "USD", 1)
            )

            `given there is real exchange calculator`()

            `when exchange calculator converts`(
                Money(10, "EUR"),
                Currency.EUR
            )
            `then exchange calculator returns`(
                Money(10, "EUR"),
            )
        }
    }

    @Test
    fun `exchange currencies - but rate not found`() = runTest {
        hotestCtx {
            `given there is a fake 'exchange rates' service`()
            `given 'exchange rates' returns rates`(
                ExchangeRate("EUR", "PLN", 4),
                ExchangeRate("EUR", "CHF", 2),
                ExchangeRate("EUR", "USD", 1)
            )

            `given there is real exchange calculator`()

            `when exchange calculator converts`(
                Money(10, "PLN"),
                Currency.CHF
            )
            `then exchange calculator reports no rate error`()
        }
    }
}