package com.golinko.investment.portfolio.client

import com.golinko.investment.fmp.api.HistoryApi
import com.golinko.investment.fmp.model.EndOfDayPriceHistory
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import org.junit.jupiter.api.extension.ExtendWith
import org.openapitools.client.infrastructure.ServerException
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class HistoryClientTest {

    @InjectMockKs
    private lateinit var historyClient: HistoryClient

    @MockK
    private lateinit var historyApi: HistoryApi
    private val apiKey: String = "apiKey"
    @MockK
    private lateinit var endOfDayPriceHistory: EndOfDayPriceHistory

    private val ticker = "TICKER"
    private val from = LocalDate.of(2010, 10, 10)
    private val to = LocalDate.of(2010, 12, 10)

    @BeforeEach
    fun setUp() {
        every { historyApi.getDailyPrices(ticker, apiKey, from, to, "line") } returns endOfDayPriceHistory
    }

    @Test
    fun dailyPrices() {
        val result = historyClient.dailyPrices(ticker, from, to)

        assertThat(result).isEqualTo(endOfDayPriceHistory)
    }

    @Test
    fun `dailyPrices failure`() {
        every { historyApi.getDailyPrices(ticker, apiKey, from, to, "line") } throws ServerException("error")

        assertThrows<ServerException> { historyClient.dailyPrices(ticker, from, to) }
    }
}