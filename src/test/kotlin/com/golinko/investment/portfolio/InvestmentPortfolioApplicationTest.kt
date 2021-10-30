package com.golinko.investment.portfolio

import com.golinko.investment.portfolio.repo.RiskLevel
import com.golinko.investment.portfolio.web.Portfolio
import com.golinko.investment.portfolio.web.PortfolioFilter
import io.restassured.RestAssured.basePath
import io.restassured.RestAssured.baseURI
import io.restassured.RestAssured.given
import io.restassured.RestAssured.port
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class InvestmentPortfolioApplicationTest {
    @LocalServerPort
    var localServerPort: Int? = null

    @BeforeAll
    internal fun setUp() {
        baseURI = "http://localhost"
        port = localServerPort!!
        basePath = "/users/me/investment-portfolio"
    }

    @ParameterizedTest(name = "getPortfolio returns portfolio by risk level: {0}")
    @EnumSource(value = RiskLevel::class, mode = EnumSource.Mode.MATCH_ALL)
    fun `getPortfolio returns portfolio by risk level`(riskLevel: RiskLevel) {
        val portfolios = given()
            .contentType(ContentType.JSON)
            .body(PortfolioFilter(riskLevel))
            .`when`()
            .post()
            .then()
            .assertThat()
            .statusCode(200)
            .extract().response().body.jsonPath()
            .getList(".", Portfolio::class.java)

        assertNotNull(portfolios)
        assertThat(portfolios).isNotEmpty
    }

    @Test
    fun `getPortfolio returns 400 by unknown riskLevel`() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"risk\": \"BOGUS_RISK\"}")
            .`when`()
            .post()
            .then()
            .assertThat()
            .statusCode(400)
    }
}