package com.golinko.investment.portfolio.client

import com.golinko.investment.fmp.api.HistoryApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HistoryClient(
    private val historyApi: HistoryApi,
    @Value("\${fmp.api-key}")
    private val apiKey: String
) {
    fun dailyPrices(ticker: String, from: LocalDate, to: LocalDate) =
        historyApi.getDailyPrices(ticker, apiKey, from, to, SERIE_TYPE_LINE)

    companion object {
        private const val SERIE_TYPE_LINE = "line"
    }
}