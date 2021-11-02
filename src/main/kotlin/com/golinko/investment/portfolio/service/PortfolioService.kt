package com.golinko.investment.portfolio.service

import com.golinko.investment.fmp.model.EndOfDayPrice
import com.golinko.investment.fmp.model.EndOfDayPriceHistory
import com.golinko.investment.portfolio.client.HistoryClient
import com.golinko.investment.portfolio.model.PortfolioModel
import com.golinko.investment.portfolio.model.PortfolioSettings
import com.golinko.investment.portfolio.model.RiskLevel
import com.golinko.investment.portfolio.repo.PortfolioSettingsRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class PortfolioService(
    private val portfolioSettingsRepository: PortfolioSettingsRepository,
    private val historyClient: HistoryClient
) {
    fun portfolioSettings(risk: RiskLevel) = portfolioSettingsRepository.findByRisk(risk)

    fun portfolio(
        risk: RiskLevel,
        from: LocalDate,
        to: LocalDate,
        contribution: BigDecimal
    ): List<PortfolioModel> {
        val portfolioSettings = portfolioSettings(risk)

        return portfolioSettings.map { setting -> toPortfolioModel(setting, from, to, contribution) }.flatten()
    }

    private fun toPortfolioModel(
        portfolioSettings: PortfolioSettings,
        from: LocalDate,
        to: LocalDate,
        contribution: BigDecimal,
    ): List<PortfolioModel> {
        val monthlyPrices = monthlyPriceHistory(portfolioSettings.ticker, from, to)
        val contributionPart = contribution.times(portfolioSettings.weight)

        return monthlyPrices.historical!!.map { priceData ->
            PortfolioModel(
                ticker = portfolioSettings.ticker,
                value = contributionPart,
                shares = contributionPart.divide(priceData.close, 2, RoundingMode.HALF_EVEN),
                date = priceData.date
            )
        }
    }

    private fun monthlyPriceHistory(
        ticker: String,
        from: LocalDate,
        to: LocalDate,
    ): EndOfDayPriceHistory {
        val dailyPrices = historyClient.dailyPrices(ticker, from, to)

        return EndOfDayPriceHistory(
            dailyPrices.symbol,
            openingDayPriceHistory(dailyPrices.historical ?: emptyList())
        )
    }

    private fun openingDayPriceHistory(tickerHistory: List<EndOfDayPrice>): List<EndOfDayPrice> =
        tickerHistory
            .groupBy { historicalData -> Pair(historicalData.date.year, historicalData.date.month) }
            .mapValues { monthlyData -> monthlyData.value.sortedBy { it.date.dayOfMonth }.take(1) }
            .flatMap { it.value }
}