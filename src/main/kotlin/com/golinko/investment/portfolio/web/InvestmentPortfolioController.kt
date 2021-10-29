package com.golinko.investment.portfolio.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Investment portfolio API", description = "Investment portfolio related operations")
@RestController
@RequestMapping(value = ["/users/me/investment-portfolio"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InvestmentPortfolioController(
) {
    @Operation(summary = "Return portfolio that matches the risk level of the user")
    @PostMapping
    fun getPortfolio(
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Not implemented yet")
    }

    @Operation(summary = "Calculate current value of users portfolio")
    @PostMapping("/current-value")
    fun getPortfolioCurrentValue(
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Not implemented yet")
    }
}