package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.model.RiskLevel
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate

@Schema(title ="Portfolio filter")
data class PortfolioFilter(
    @Schema(title = "Risk level")
    val risk: RiskLevel
)

@Schema(title ="Portfolio value filter")
data class PortfolioValueFilter(
    @Schema(title = "Risk level")
    val risk: RiskLevel,

    @Schema(title = "From date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val from: LocalDate,

    @Schema(title = "To date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val to: LocalDate,

    @Schema(title = "Monthly contribution")
    val contribution: BigDecimal,
)

@Schema(title ="Portfolio settings")
data class PortfolioSettingsDTO(
    @Schema(title = "Stock wighting")
    val weight: BigDecimal,
    @Schema(title = "Stock name")
    val ticker: String
)

@Schema(title ="Portfolio current value value")
data class PortfolioAggregatedDTO(
    @Schema(title = "Stock name")
    val ticker: String,
    @Schema(title = "The sum of stock contributions for the given period")
    val contribution: BigDecimal,
    @Schema(title = "The current value of stock portfolio for the given period")
    val value: BigDecimal,
    @Schema(title = "The amount of stocks in portfolio for the given period")
    val shares: BigDecimal,
)