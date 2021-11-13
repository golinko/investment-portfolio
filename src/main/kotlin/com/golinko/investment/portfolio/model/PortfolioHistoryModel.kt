package com.golinko.investment.portfolio.model

import java.math.BigDecimal
import java.time.LocalDate

data class PortfolioValueModel(
    val ticker: String,
    val currentPrice: BigDecimal,
    val portfolioHistory: List<PortfolioHistoryModel>
)

data class PortfolioHistoryModel(
    val contribution: BigDecimal,
    val shares: BigDecimal,
    val date: LocalDate,
)

data class PortfolioAggregatedModel(
    val ticker: String,
    val shares: BigDecimal,
    val value: BigDecimal,
    val contribution: BigDecimal,
)