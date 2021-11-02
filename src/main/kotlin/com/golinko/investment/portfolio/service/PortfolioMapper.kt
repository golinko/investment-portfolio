package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.model.PortfolioAggregatedModel
import com.golinko.investment.portfolio.model.PortfolioSettings
import com.golinko.investment.portfolio.web.PortfolioAggregatedDTO
import com.golinko.investment.portfolio.web.PortfolioSettingsDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PortfolioMapper {
    fun mapPortfolioSettings(portfolioSettings: List<PortfolioSettings>): List<PortfolioSettingsDTO>

    fun mapPortfolioSettings(portfolioSettings: PortfolioSettings): PortfolioSettingsDTO

    fun mapPortfolio(portfolioHistory: List<PortfolioAggregatedModel>): List<PortfolioAggregatedDTO>
}