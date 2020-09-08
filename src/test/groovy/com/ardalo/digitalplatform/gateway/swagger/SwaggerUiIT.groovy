package com.ardalo.digitalplatform.gateway.swagger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
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
      .uri("/swagger-ui/index.html")
      .exchange()
      .expectStatus().isOk()
      .expectBody(String).consumeWith({ res ->
        assert res.responseBody.contains("<title>Swagger UI</title>")
      })
  }

  def "should redirect GET / to /swagger-ui/index.html"() {
    expect:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri("/")
      .exchange()
      .expectStatus().isEqualTo(HttpStatus.PERMANENT_REDIRECT)
      .expectHeader().valueEquals("Location", "/swagger-ui/index.html")
      .expectBody().isEmpty()
  }
}
