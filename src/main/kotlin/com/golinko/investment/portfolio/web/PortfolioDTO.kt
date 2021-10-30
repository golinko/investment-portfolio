package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.repo.RiskLevel
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title ="Portfolio filter")
data class PortfolioFilter(
    @Schema(title = "Risk level")
    val risk: RiskLevel
)

@Schema(title ="Portfolio")
data class Portfolio(
    @Schema(title = "Stock wighting")
    val weight: Float,
    @Schema(title = "Stock name")
    val ticker: String
)