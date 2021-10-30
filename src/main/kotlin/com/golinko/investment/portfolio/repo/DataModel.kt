package com.golinko.investment.portfolio.repo

import org.springframework.core.convert.converter.Converter
import org.springframework.data.annotation.Id
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.relational.core.mapping.Table


@Table
data class Stock(
    @Id val id: String?,
    val name: String,
    val risk: RiskLevel,
    val weight: Float,
)

enum class RiskLevel(val risk: String) {
    LOW("2"),
    MODERATELY_LOW("4"),
    MODERATE("6"),
    MODERATELY_HIGH("8"),
    HIGH("10");
}

@WritingConverter
class RiskLevelToStringConverter: Converter<RiskLevel?, String?> {
    override fun convert(source: RiskLevel): String = source.risk
}

@ReadingConverter
class StringToRiskLevelConverter: Converter<String?, RiskLevel?> {
    override fun convert(source: String): RiskLevel? = RiskLevel.values().find { it.risk == source }
}