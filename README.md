# Ardalo Digital Platform Gateway
![Build Status](https://github.com/ardalo/platform-gateway/workflows/Build/badge.svg)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=ardalo_platform-gateway&metric=coverage)](https://sonarcloud.io/dashboard?id=ardalo_platform-gateway)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=ardalo_platform-gateway&metric=ncloc)](https://sonarcloud.io/dashboard?id=ardalo_platform-gateway)

Platform Gateway of the Ardalo Digital Platform.

## Tech Info
__Java Spring Cloud Gateway__
* Java 11
* Gradle
* Spring Cloud Gateway
* Spock
* Prometheus Metrics
* Swagger UI
* Access and Application Logs in JSON format
* JaCoCo Code Coverage Report
* Static Code Analysis via SonarCloud
* Docker
* CI/CD: GitHub Actions

## Quick Start
* Run via Docker using `docker-compose` and find API docs at `http://localhost:8080/gateway`
  ```console
  $ docker-compose build && docker-compose up
  ```
* Start application and find API docs at `http://localhost:8080/gateway`
  ```console
  $ ./gradlew bootRun
  ```
* Run tests
  ```console
  $ ./gradlew test
  ```
* Generate Code Coverage Report. HTML Report can be found in `./build/reports/jacoco/test/html`
  ```console
  $ ./gradlew check jacocoTestReport
  ```

## API Documentation
Swagger UI is accessible via `/gateway/apidoc` (e.g. http://localhost:8080/gateway/apidoc).
It provides an overview of all endpoints.

## Create Platform Routes
All incoming requests approach at this Platform Gateway. In order to route those requests to other services, the
corresponding platform routes need to be created at this Platform Gateway.

Platform routes are created via REST API calls: `POST /gateway/api/routes/v1/{routeId}`. The request body contains the route
definition in [Spring Cloud Gateway](https://cloud.spring.io/spring-cloud-gateway/reference/html/#creating-and-deleting-a-particular-route)
format.

Example #1: Frontpage - Route requests to root path "/" to backend service
* `POST /gateway/api/routes/v1/frontpage`
```json
{
  "uri":"http://frontpage-service:8080",
  "predicates": [
    {"name":"Path","args":{"arg0":"/"}}
  ],
  "filters": [
    {"name":"RewritePath","args":{"regexp":".+","replacement":"/api/pages/frontpage"}}
  ]
}
```

Example #2: Product Details Page - Route requests to "/products/{productId}" and "/products/{seo-friendly-product-title}/{productId}" to backend service
* `POST /gateway/api/routes/v1/product-details-page-without-product-title-in-url`
```json
{
  "uri":"http://product-service:8080",
  "predicates": [
    {"name":"Path","args":{"arg0":"/p/{productId}"}}
  ],
  "filters": [
    {"name":"RewritePath","args":{"regexp":"/p/(?<productId>.+)","replacement":"/api/pages/product-details-page/${productId}"}}
  ]
}
```
* `POST /gateway/api/routes/v1/product-details-page-with-product-title-in-url`
```json
{
  "uri":"http://product-service:8080",
  "predicates": [
    {"name":"Path","args":{"arg0":"/p/{productTitle}/{productId}"}}
  ],
  "filters": [
    {"name":"RewritePath","args":{"regexp":"/p/(?<productTitle>.+?)/(?<productId>.+)","replacement":"/api/pages/product-details-page/${productId}"}}
  ]
}
```
