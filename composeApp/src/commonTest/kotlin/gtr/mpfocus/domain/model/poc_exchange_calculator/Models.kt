package gtr.mpfocus.domain.model.poc_exchange_calculator

object Models {

    data class ExchangeRate(
        val from: String,
        val to: String,
        val rate: Long
    )

    data class Money(
        val amount: Long,
        val currency: String,
    )
}