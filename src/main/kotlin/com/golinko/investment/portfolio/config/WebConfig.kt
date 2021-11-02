package com.golinko.investment.portfolio.config

import com.golinko.investment.fmp.api.HistoryApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebConfig {

    @Bean
    fun fmpHistoryPriceApi(): HistoryApi {
        return HistoryApi()
    }

}