package com.golinko.investment.portfolio.service

import com.golinko.investment.fmp.model.EndOfDayPrice
import com.golinko.investment.fmp.model.EndOfDayPriceHistory
import com.golinko.investment.portfolio.client.HistoryClient
import com.golinko.investment.portfolio.model.PortfolioHistoryModel
import com.golinko.investment.portfolio.model.PortfolioSettings
import com.golinko.investment.portfolio.model.PortfolioValueModel
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

    fun portfolioValue(
        risk: RiskLevel,
        from: LocalDate,
        to: LocalDate,
        contribution: BigDecimal
    ): List<PortfolioValueModel> =
        portfolioSettings(risk)
            .map { tickerSettings -> tickerPortfolio(tickerSettings, from, to, contribution) }


    private fun tickerPortfolio(
        tickerSettings: PortfolioSettings,
        from: LocalDate,
        to: LocalDate,
        contribution: BigDecimal,
    ): PortfolioValueModel {
        val dailyPrices = historyClient.dailyPrices(tickerSettings.ticker, from, to)
        val monthlyPrices = monthlyPriceHistory(dailyPrices)
        val lastDayPrice = lastDayPriceHistory(dailyPrices.historical ?: emptyList())
        val contributionPart = contribution.multiply(tickerSettings.weight)
        val tickerHistory = monthlyPrices.historical!!.map { priceData ->
            PortfolioHistoryModel(
                contribution = contributionPart,
                shares = contributionPart.divide(priceData.close, 5, RoundingMode.HALF_EVEN),
                date = priceData.date
            )
        }

        return PortfolioValueModel(
            ticker = tickerSettings.ticker,
            currentPrice = lastDayPrice?.close ?: BigDecimal.ZERO,
            portfolioHistory = tickerHistory
        )
    }

    private fun monthlyPriceHistory(dailyPrices: EndOfDayPriceHistory) =
        EndOfDayPriceHistory(
            dailyPrices.symbol,
            openingDayPriceHistory(dailyPrices.historical ?: emptyList())
        )

    private fun openingDayPriceHistory(tickerHistory: List<EndOfDayPrice>): List<EndOfDayPrice> =
        tickerHistory
            .groupBy { historicalData -> Pair(historicalData.date.year, historicalData.date.month) }
            .mapValues { monthlyData -> monthlyData.value.sortedBy { it.date.dayOfMonth }.take(1) }
            .flatMap { it.value }

    private fun lastDayPriceHistory(tickerHistory: List<EndOfDayPrice>) = tickerHistory.maxByOrNull { it.date }
}