package com.golinko.investment.portfolio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InvestmentPortfolioApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<InvestmentPortfolioApplication>(*args)
		}
	}
}





