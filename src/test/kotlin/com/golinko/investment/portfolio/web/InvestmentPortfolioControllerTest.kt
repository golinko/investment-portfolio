package com.golinko.investment.portfolio.web

import com.golinko.investment.portfolio.model.PortfolioAggregatedModel
import com.golinko.investment.portfolio.model.PortfolioSettings
import com.golinko.investment.portfolio.model.PortfolioValueModel
import com.golinko.investment.portfolio.model.RiskLevel
import com.golinko.investment.portfolio.service.PortfolioAggregatorService
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
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class InvestmentPortfolioControllerTest {

    @InjectMockKs
    private lateinit var investmentPortfolioController: InvestmentPortfolioController

    @MockK
    private lateinit var portfolioService: PortfolioService
    @MockK
    private lateinit var aggregatorService: PortfolioAggregatorService
    @MockK
    private lateinit var portfolioMapper: PortfolioMapper

    @MockK
    private lateinit var portfolioSettingsDTO: PortfolioSettingsDTO
    @MockK
    private lateinit var portfolioSettings: List<PortfolioSettings>
    @MockK
    private lateinit var portfolioValueModels: List<PortfolioValueModel>
    @MockK
    private lateinit var aggregated: List<PortfolioAggregatedModel>
    @MockK
    private lateinit var mapped: PortfolioAggregatedDTO

    private val portfolioFilter = PortfolioFilter(RiskLevel.MODERATELY_HIGH)
    private val valueFilter = PortfolioValueFilter(RiskLevel.MODERATE, LocalDate.now(), LocalDate.now(), BigDecimal("500"))

    @BeforeEach
    fun setUp() {
        every { portfolioService.portfolioSettings(portfolioFilter.risk) } returns portfolioSettings
        every {
            portfolioService.portfolioValue(valueFilter.risk, valueFilter.from, valueFilter.to, valueFilter.contribution)
        } returns portfolioValueModels
        every { aggregatorService.aggregatePortfolioValue(portfolioValueModels) } returns aggregated

        every { portfolioMapper.mapPortfolioSettings(portfolioSettings) } returns listOf(portfolioSettingsDTO)
        every { portfolioMapper.mapPortfolio(aggregated) } returns listOf(mapped)
    }

    @Test
    fun `getPortfolioCurrentValue returns portfolio current value`() {
        val result = investmentPortfolioController.getPortfolioCurrentValue(valueFilter)

        assertNotNull(result)
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(mapped)
    }

    @Test
    fun `getPortfolioCurrentValue throws exception`() {
        every {
            portfolioService.portfolioValue(valueFilter.risk, valueFilter.from, valueFilter.to, valueFilter.contribution)
        } throws IllegalArgumentException("Some exception")

        assertThrows<IllegalArgumentException> { investmentPortfolioController.getPortfolioCurrentValue(valueFilter) }
    }

    @Test
    fun `getPortfolioSettings returns settings by risk level`() {
        val result = investmentPortfolioController.getPortfolioSettings(portfolioFilter)

        assertNotNull(result)
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(portfolioSettingsDTO)
    }

    @Test
    fun `getPortfolioSettings throws exception`() {
        every { portfolioService.portfolioSettings(portfolioFilter.risk) } throws IllegalArgumentException("Invalid risk level")

        assertThrows<IllegalArgumentException> { investmentPortfolioController.getPortfolioSettings(portfolioFilter) }
    }
}