# Investment portfolio API

API for generation of investment portfolios

## Getting Started

These instructions will get you the project up and running on your
local machine

### Prerequisites

For building and running the application you need:

- [JDK 13](https://jdk.java.net/13/)
- [Maven](https://maven.apache.org/)

## Running the application locally

In order to run the application you will need to set up your [FMP personall api key](https://financialmodelingprep.com/developer/docs)
and use maven to run the application:

```shell
mvn spring-boot:run -Dspring-boot.run.arguments=--fmp.api-key=[your-api-key-here]
```

If you want to start the app from your IDE make sure you set up the api key in the [application.yml](src/main/resources/application.yml)
and run 

```shell
mvn clean install
```

## API Specification + TryItOut

After the application is up and running you can visit [Swagger UI](http://localhost:8080/swagger-ui.html)
to get the API endpoints specifications.
There you can also try them out by clicking on the `Try it out` button

## Running the tests

```shell
mvn clean verify
```

## Integration test

There is an integration test [InvestmentPortfolioApplicationTest](com.golinko.investment.portfolio.InvestmentPortfolioApplicationTest)
which connects to the FMP API directly without mocking it. This test is excluded from build.
In order to run it you need to set up the api key in the [application.yml](src/main/resources/application.yml)