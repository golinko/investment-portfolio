package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.repo.RiskLevel
import com.golinko.investment.portfolio.repo.Stock
import com.golinko.investment.portfolio.repo.StocksRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class PortfolioServiceTest {

    @InjectMockKs
    private lateinit var portfolioService: PortfolioService

    @MockK
    private lateinit var stocksRepository: StocksRepository
    @MockK
    private lateinit var stock: Stock

    @Test
    fun `portfolio returns stocks by risk level`() {
        every { stocksRepository.findByRisk(RiskLevel.HIGH) } returns listOf(stock)

        val result = portfolioService.portfolio(RiskLevel.HIGH)

        assertNotNull(result)
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(stock)
    }

    @Test
    fun `portfolio returns empty if no stocks found`() {
        every { stocksRepository.findByRisk(RiskLevel.LOW) } returns emptyList()

        val result = portfolioService.portfolio(RiskLevel.LOW)

        assertNotNull(result)
        assertThat(result).isEmpty()
    }

    @Test
    fun `portfolio throws exception`() {
        every { stocksRepository.findByRisk(RiskLevel.MODERATELY_LOW) } throws IllegalArgumentException("Invalid risk level")

        assertThrows<IllegalArgumentException> { portfolioService.portfolio(RiskLevel.MODERATELY_LOW) }
    }
}