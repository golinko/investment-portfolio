package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.model.PortfolioAggregatedModel
import com.golinko.investment.portfolio.model.PortfolioValueModel
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service
class PortfolioAggregatorService {

    fun aggregatePortfolioValue(portfolioValue: List<PortfolioValueModel>): List<PortfolioAggregatedModel> =
        portfolioValue.map { portfolio ->
            val shares = portfolio.portfolioHistory.sumOf { it.shares }
            PortfolioAggregatedModel(
                ticker = portfolio.ticker,
                shares = shares.setScale(2, RoundingMode.HALF_UP),
                contribution = portfolio.portfolioHistory.sumOf { it.contribution }.setScale(2, RoundingMode.HALF_UP),
                value = portfolio.currentPrice.multiply(shares).setScale(2, RoundingMode.HALF_UP)
            )
        }
}