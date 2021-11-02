package com.golinko.investment.portfolio.service

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.golinko.investment.fmp.model.EndOfDayPriceHistory
import com.golinko.investment.portfolio.client.HistoryClient
import com.golinko.investment.portfolio.model.PortfolioModel
import com.golinko.investment.portfolio.model.PortfolioSettings
import com.golinko.investment.portfolio.model.RiskLevel
import com.golinko.investment.portfolio.repo.PortfolioSettingsRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.apache.commons.io.IOUtils.resourceToString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.nio.charset.StandardCharsets.UTF_8
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class PortfolioServiceTest {

    @InjectMockKs
    private lateinit var portfolioService: PortfolioService

    @MockK
    private lateinit var portfolioSettingsRepository: PortfolioSettingsRepository
    @MockK
    private lateinit var historyClient: HistoryClient

    @MockK
    private lateinit var portfolioSettings: PortfolioSettings

    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    private val risk = RiskLevel.HIGH
    private val ticker = "CAKE"
    private val from = LocalDate.of(2017, 1, 1)
    private val to = LocalDate.of(2019, 1, 1)
    private val contribution = BigDecimal("200")
    private val weight = BigDecimal("0.5")

    @BeforeEach
    fun setUp() {
        every { portfolioSettingsRepository.findByRisk(risk) } returns listOf(portfolioSettings)
        every { portfolioSettings.ticker } returns ticker
        every { portfolioSettings.weight } returns weight
    }

    @Test
    fun `portfolio returns history for opening days only`() {
        every { historyClient.dailyPrices(ticker, from, to) } returns
                mapper.readValue(resourceToString("/fmp-test-data.json", UTF_8))

        val result = portfolioService.portfolio(risk, from, to, contribution)

        assertEquals(
            mapper.readValue<List<PortfolioModel>>(resourceToString("/expected-portfolio.json", UTF_8)),
            result
        )
    }

    @Test
    fun `portfolio returns empty if no history data`() {
        every { historyClient.dailyPrices(ticker, from, to) } returns EndOfDayPriceHistory()

        val result = portfolioService.portfolio(risk, from, to, contribution)

        assertNotNull(result)
        assertThat(result).isEmpty()
    }

    @Test
    fun `portfolioSettings returns portfolio settings by risk level`() {
        val result = portfolioService.portfolioSettings(risk)

        assertNotNull(result)
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(portfolioSettings)
    }

    @Test
    fun `portfolioSettings returns empty if no portfolio settings found`() {
        every { portfolioSettingsRepository.findByRisk(risk) } returns emptyList()

        val result = portfolioService.portfolioSettings(risk)

        assertNotNull(result)
        assertThat(result).isEmpty()
    }

    @Test
    fun `portfolioSettings throws exception`() {
        every { portfolioSettingsRepository.findByRisk(risk) } throws IllegalArgumentException("Invalid risk level")

        assertThrows<IllegalArgumentException> { portfolioService.portfolioSettings(risk) }
    }

}