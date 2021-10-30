package com.golinko.investment.portfolio.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StocksRepository: CrudRepository<Stock, Long> {
    fun findByRisk(risk: String?): List<Stock>
}