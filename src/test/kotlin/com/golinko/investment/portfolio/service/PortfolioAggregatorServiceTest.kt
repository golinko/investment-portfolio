package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.model.PortfolioModel
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class PortfolioAggregatorServiceTest {

    @InjectMockKs
    private lateinit var portfolioAggregatorService: PortfolioAggregatorService

    private val ticker1 = "TICKER1"
    private val ticker2 = "TICKER2"
    private val date = LocalDate.now()

    private val portfolioModels = listOf(
        PortfolioModel(ticker1, BigDecimal("100"), BigDecimal("1.1"), date),
        PortfolioModel(ticker1, BigDecimal("100"), BigDecimal("1.2"), date),
        PortfolioModel(ticker2, BigDecimal("200"), BigDecimal("3.5"), date),
        PortfolioModel(ticker2, BigDecimal("200"), BigDecimal("3.5"), date),
    )

    @Test
    fun `aggregatePortfolio aggregates shares and contributions per ticker`() {
        val result = portfolioAggregatorService.aggregatePortfolio(portfolioModels)

        assertThat(result).hasSize(2)

        val ticker1Result = result.find { it.ticker == ticker1 }
        assertNotNull(ticker1Result)
        assertThat(ticker1Result!!.value == BigDecimal("200"))
        assertThat(ticker1Result.shares == BigDecimal("2.3"))

        val ticker2Result = result.find { it.ticker == ticker2 }
        assertNotNull(ticker2Result)
        assertThat(ticker2Result!!.value == BigDecimal("400"))
        assertThat(ticker2Result.shares == BigDecimal("7"))
    }
}