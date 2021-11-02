package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.service.PortfolioAggregatorService
import com.golinko.investment.portfolio.service.PortfolioMapper
import com.golinko.investment.portfolio.service.PortfolioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Investment portfolio API", description = "Investment portfolio related operations")
@RestController
@RequestMapping(value = ["/users/me/investment-portfolio"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InvestmentPortfolioController(
    private val portfolioService: PortfolioService,
    private val aggregatorService: PortfolioAggregatorService,
    private val portfolioMapper: PortfolioMapper
) {
    @Operation(summary = "Return portfolio settings that matches the risk level of the user")
    @PostMapping
    fun getPortfolioSettings(
        @Parameter(required = true, description = "Filter") @RequestBody filter: PortfolioFilter
    ): List<PortfolioSettingsDTO> = portfolioMapper.mapPortfolioSettings(
        portfolioService.portfolioSettings(filter.risk)
    )

    @Operation(summary = "Calculate current value of users portfolio")
    @PostMapping("/current-value")
    fun getPortfolioCurrentValue(
        @Parameter(required = true, description = "Filter") @RequestBody filter: PortfolioValueFilter
    ): List<PortfolioAggregatedDTO> {
        val portfolio = portfolioService.portfolio(filter.risk, filter.from, filter.to, filter.contribution)
        val aggregated = aggregatorService.aggregatePortfolio(portfolio)
        return portfolioMapper.mapPortfolio(aggregated)
    }
}