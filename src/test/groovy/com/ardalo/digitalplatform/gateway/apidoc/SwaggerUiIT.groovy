package com.ardalo.digitalplatform.gateway.apidoc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest
@AutoConfigureWebTestClient
class SwaggerUiIT extends Specification {

  @Autowired
  ApplicationContext applicationContext

  def "should provide Swagger UI"() {
    expect:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri("/gateway/apidoc/swagger-ui/index.html")
      .exchange()
      .expectStatus().isOk()
      .expectBody(String).consumeWith({ res ->
        assert res.responseBody.contains("<title>Swagger UI</title>")
      })
  }

  def "should redirect GET /gateway/apidoc to /gateway/apidoc/swagger-ui/index.html"() {
    expect:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri(requestPath)
      .exchange()
      .expectStatus().isEqualTo(HttpStatus.PERMANENT_REDIRECT)
      .expectHeader().valueEquals("Location", "/gateway/apidoc/swagger-ui/index.html")
      .expectBody().isEmpty()

    where:
    requestPath << ["/gateway/apidoc", "/gateway/apidoc/"]
  }

  def "should provide OpenAPI v3 API doc"() {
    expect:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri("/gateway/apidoc/v3")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().jsonPath('$.openapi').isEqualTo("3.0.3")
  }

  def "should provide Swagger v2 API doc"() {
    expect:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri("/gateway/apidoc/v2")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().jsonPath('$.swagger').isEqualTo("2.0")
  }
}
