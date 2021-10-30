package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.service.PortfolioMapper
import com.golinko.investment.portfolio.service.PortfolioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Investment portfolio API", description = "Investment portfolio related operations")
@RestController
@RequestMapping(value = ["/users/me/investment-portfolio"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InvestmentPortfolioController(
    private val portfolioService: PortfolioService,
    private val portfolioMapper: PortfolioMapper
) {
    @Operation(summary = "Return portfolio that matches the risk level of the user")
    @PostMapping
    fun getPortfolio(
        @RequestBody portfolioFilter: PortfolioFilter
    ): ResponseEntity<List<Portfolio>> {
        val stocks = portfolioService.portfolio(portfolioFilter.risk)
        return ResponseEntity.ok(portfolioMapper.map(stocks))
    }

    @Operation(summary = "Calculate current value of users portfolio")
    @PostMapping("/current-value")
    fun getPortfolioCurrentValue(
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Not implemented yet")
    }
}