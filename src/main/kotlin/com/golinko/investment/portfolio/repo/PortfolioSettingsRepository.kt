package com.golinko.investment.portfolio.repo

import com.golinko.investment.portfolio.model.PortfolioSettings
import com.golinko.investment.portfolio.model.RiskLevel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PortfolioSettingsRepository: CrudRepository<PortfolioSettings, Long> {
    fun findByRisk(risk: RiskLevel): List<PortfolioSettings>
}