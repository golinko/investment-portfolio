package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.service.PortfolioAggregatorService
import com.golinko.investment.portfolio.service.PortfolioMapper
import com.golinko.investment.portfolio.service.PortfolioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "Investment portfolio API", description = "Investment portfolio related operations")
@RestController
@RequestMapping(value = ["/users/me/investment-portfolio"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InvestmentPortfolioController(
    private val portfolioService: PortfolioService,
    private val aggregatorService: PortfolioAggregatorService,
    private val portfolioMapper: PortfolioMapper
) {
    @Operation(summary = "Return portfolio settings that matches the risk level of the user")
    @GetMapping
    fun getPortfolioSettings(
        @Parameter(required = true, description = "Filter") @Valid filter: PortfolioFilter
    ): List<PortfolioSettingsDTO> = portfolioMapper.mapPortfolioSettings(
        portfolioService.portfolioSettings(filter.risk)
    )

    @Operation(summary = "Calculate current value of users portfolio")
    @GetMapping("/current-value")
    fun getPortfolioCurrentValue(
        @Parameter(required = true, description = "Filter") @Valid filter: PortfolioValueFilter
    ): List<PortfolioAggregatedDTO> {
        val portfolioValue = portfolioService.portfolioValue(filter.risk, filter.from, filter.to, filter.contribution)
        val aggregated = aggregatorService.aggregatePortfolioValue(portfolioValue)
        return portfolioMapper.mapPortfolio(aggregated)
    }
}