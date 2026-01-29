package gtr.mpfocus.domain.model.poc_exchange_calculator

import kotlin.test.assertEquals

object Assertions {

    fun moneyEquals(expected: Models.Money, actual: Money) {
        assertEquals(expected.amount, actual.amount)
        assertEquals(Currency.valueOf(expected.currency), actual.currency)
    }
}