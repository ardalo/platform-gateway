package com.ardalo.digitalplatform.gateway.health

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest
@AutoConfigureWebTestClient
class HealthControllerIT extends Specification {

  def webTestClient = WebTestClient.bindToController(HealthController).build()

  def "should return 200 OK for GET /alive"() {
    expect:
    webTestClient
      .get()
      .uri("/alive")
      .exchange()
      .expectStatus().isOk()
      .expectBody().isEmpty()
  }

  def "should return 200 OK for GET /ready"() {
    expect:
    webTestClient
      .get()
      .uri("/ready")
      .exchange()
      .expectStatus().isOk()
      .expectBody().isEmpty()
  }
}
