package com.golinko.investment.portfolio

import com.golinko.investment.portfolio.model.RiskLevel
import com.golinko.investment.portfolio.web.PortfolioAggregatedDTO
import com.golinko.investment.portfolio.web.PortfolioSettingsDTO
import io.restassured.RestAssured.basePath
import io.restassured.RestAssured.baseURI
import io.restassured.RestAssured.given
import io.restassured.RestAssured.port
import io.restassured.http.ContentType
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.math.BigDecimal
import java.nio.charset.StandardCharsets.UTF_8

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
    fun `getPortfolioSettings returns portfolio settings by risk level`(riskLevel: RiskLevel) {
        val settings = given()
            .contentType(ContentType.JSON)
            .queryParam("risk", riskLevel)
            .`when`()
            .get()
            .then()
            .assertThat()
            .statusCode(200)
            .extract().response().body.jsonPath()
            .getList(".", PortfolioSettingsDTO::class.java)

        assertNotNull(settings)
        assertThat(settings).isNotEmpty
    }

    @Test
    fun `getPortfolioSettings returns 400 by unknown riskLevel`() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("risk", "BOGUS_RISK")
            .`when`()
            .get()
            .then()
            .assertThat()
            .statusCode(400)
    }

    @Test
    fun `getPortfolioCurrentValue returns accumulated current value for one day`() {
        val portfolios = given()
            .contentType(ContentType.JSON)
            .queryParam("risk", RiskLevel.MODERATE)
            .queryParam("from", "2017-01-01")
            .queryParam("to", "2017-01-03")
            .queryParam("contribution", 450)
            .`when`()
            .get("/current-value")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().response().body.asString()

        JSONAssert.assertEquals(IOUtils.resourceToString("/expected-api-portfolio-2017-01-03.json", UTF_8), portfolios, true)
    }

    @Test
    fun `getPortfolioCurrentValue returns accumulated current value for multiple years`() {
        val portfolios = given()
            .contentType(ContentType.JSON)
            .queryParam("risk", RiskLevel.MODERATE)
            .queryParam("from", "2017-01-01")
            .queryParam("to", "2021-06-03")
            .queryParam("contribution", 450)
            .`when`()
            .get("/current-value")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().response().body.asString()

        JSONAssert.assertEquals(IOUtils.resourceToString("/expected-api-portfolio-2021-06-03.json", UTF_8), portfolios, true)
    }

    @Test
    fun `getPortfolioCurrentValue returns empty values if no history data`() {
        val result = given()
            .contentType(ContentType.JSON)
            .queryParam("risk", RiskLevel.MODERATE)
            .queryParam("from", "2017-01-01")
            .queryParam("to", "2017-01-01")
            .queryParam("contribution", 450)
            .`when`()
            .get("/current-value")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().response().body.jsonPath().getList( "", PortfolioAggregatedDTO::class.java)

        assertThat(result).hasSize(3)
        assertThat(result)
            .allMatch { it.value == BigDecimal("0.0") }
            .allMatch { it.contribution == BigDecimal("0.0") }
            .allMatch { it.shares == BigDecimal("0.0") }
    }
}