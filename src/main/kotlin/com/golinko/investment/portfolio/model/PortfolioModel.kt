package com.golinko.investment.portfolio.model

import java.math.BigDecimal
import java.time.LocalDate

data class PortfolioModel(
    val ticker: String,
    val value: BigDecimal,
    val shares: BigDecimal,
    val date: LocalDate,
)

data class PortfolioAggregatedModel(
    val ticker: String,
    val shares: BigDecimal,
    val value: BigDecimal,
)