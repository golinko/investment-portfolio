package com.golinko.investment.portfolio.service

import com.golinko.investment.portfolio.repo.Stock
import com.golinko.investment.portfolio.web.Portfolio
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface PortfolioMapper {
    fun map(stocks: List<Stock>): List<Portfolio>

    @Mapping(source = "name", target = "ticker")
    fun map(stock: Stock): Portfolio
}