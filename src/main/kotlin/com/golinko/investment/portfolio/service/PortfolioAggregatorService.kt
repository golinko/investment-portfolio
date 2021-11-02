package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.model.PortfolioAggregatedModel
import com.golinko.investment.portfolio.model.PortfolioModel
import org.springframework.stereotype.Service

@Service
class PortfolioAggregatorService {

    fun aggregatePortfolio(portfolioModels: List<PortfolioModel>): List<PortfolioAggregatedModel> =
        portfolioModels.groupBy { it.ticker }
            .mapValues { portfolio ->
                PortfolioAggregatedModel(
                    ticker = portfolio.key,
                    shares = portfolio.value.sumOf { it.shares },
                    value = portfolio.value.sumOf { it.value },
                )
            }.values.toList()
}