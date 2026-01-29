package gtr.mpfocus.domain.model.poc_exchange_calculator

class FakeRatesProvider : RatesProvider {

    lateinit var fakeRates: List<ExchangeRate>

    override fun getRates(): List<ExchangeRate> {
        return fakeRates
    }
}