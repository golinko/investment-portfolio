package com.golinko.investment.portfolio.config

import com.golinko.investment.portfolio.repo.RiskLevelToStringConverter
import com.golinko.investment.portfolio.repo.StringToRiskLevelConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration


@Configuration
class JdbcConfig: AbstractJdbcConfiguration() {
    @Bean
    override fun jdbcCustomConversions(): JdbcCustomConversions {
        return JdbcCustomConversions(listOf(RiskLevelToStringConverter(), StringToRiskLevelConverter()))
    }
}