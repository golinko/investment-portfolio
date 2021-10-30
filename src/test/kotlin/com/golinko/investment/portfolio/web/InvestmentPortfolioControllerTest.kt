package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.repo.RiskLevel
import com.golinko.investment.portfolio.repo.Stock
import com.golinko.investment.portfolio.service.PortfolioMapper
import com.golinko.investment.portfolio.service.PortfolioService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
internal class InvestmentPortfolioControllerTest {

    @InjectMockKs
    private lateinit var investmentPortfolioController: InvestmentPortfolioController

    @MockK
    private lateinit var portfolioService: PortfolioService
    @MockK
    private lateinit var portfolioMapper: PortfolioMapper
    @MockK
    private lateinit var stock1: Stock
    @MockK
    private lateinit var portfolio: Portfolio

    private lateinit var stocks: List<Stock>

    @BeforeEach
    fun setUp() {
        stocks = listOf(stock1)
        every { portfolioService.portfolio(any()) } returns stocks
        every { portfolioMapper.map(stocks) } returns listOf(portfolio)
    }

    @Test
    fun `getPortfolio returns portfolio by risk level`() {
        val result = investmentPortfolioController.getPortfolio(PortfolioFilter(RiskLevel.MODERATELY_HIGH))

        assertNotNull(result)
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).hasSize(1)
        assertThat(result.body?.get(0)).isEqualTo(portfolio)
    }

    @Test
    fun `getPortfolio throws exception`() {
        every { portfolioService.portfolio(any()) } throws IllegalArgumentException("Invalid risk level")

        assertThrows<IllegalArgumentException> { investmentPortfolioController.getPortfolio(PortfolioFilter(RiskLevel.MODERATELY_HIGH)) }
    }
}