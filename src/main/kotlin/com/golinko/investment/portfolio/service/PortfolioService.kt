package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.repo.RiskLevel
import com.golinko.investment.portfolio.repo.StocksRepository
import org.springframework.stereotype.Service

@Service
class PortfolioService(
    private val stocksRepository: StocksRepository
) {
    fun portfolio(risk: RiskLevel) = stocksRepository.findByRisk(risk.risk)
}