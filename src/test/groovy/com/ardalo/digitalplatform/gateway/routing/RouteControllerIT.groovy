package com.ardalo.digitalplatform.gateway.routing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest
@AutoConfigureWebTestClient
class RouteControllerIT extends Specification {

  @Autowired
  ApplicationContext applicationContext

  WebTestClient webTestClient

  def setup() {
    this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build()
  }

  def "should return existing route"() {
    given:
    webTestClient
      .post()
      .uri("/gateway/api/routes/v1/test-route-003")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue('{"uri":"http://backend-service:80","predicates":[{"name":"Path","args":{"arg0":"/test"}}]}')
      .exchange()
      .expectStatus().isCreated()

    expect:
    webTestClient
      .get()
      .uri("/gateway/api/routes/v1/test-route-003")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().json('{"route_id":"test-route-003","predicate":"Paths: [/test], match trailing slash: true","filters":[],"uri":"http://backend-service:80","order":0}')

    cleanup:
    webTestClient
      .delete()
      .uri("/gateway/api/routes/v1/test-route-003")
      .exchange()
      .expectStatus().isOk()
  }

  def "should return 404 Not Found when querying non existing route"() {
    expect:
    webTestClient
      .get()
      .uri("/gateway/api/routes/v1/bdibdjhsbcvshbc")
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().isEmpty()
  }

  def "should return empty list of routes"() {
    expect:
    webTestClient
      .get()
      .uri("/gateway/api/routes/v1")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().json("[]")
  }

  def "should return existing routes"() {
    given:
    webTestClient
      .post()
      .uri("/gateway/api/routes/v1/test-route-001")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue('{"id":"foobar","uri":"http://backend-service:80","predicates":[{"name":"Path","args":{"arg0":"/test"}}]}')
      .exchange()
      .expectStatus().isCreated()

    expect:
    webTestClient
      .get()
      .uri("/gateway/api/routes/v1")
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody().json('[{"route_id":"test-route-001","predicate":"Paths: [/test], match trailing slash: true","filters":[],"uri":"http://backend-service:80","order":0}]')

    cleanup:
    webTestClient
      .delete()
      .uri("/gateway/api/routes/v1/test-route-001")
      .exchange()
      .expectStatus().isOk()
  }

  def "should create route"() {
    expect:
    webTestClient
      .post()
      .uri("/gateway/api/routes/v1/test-route-002")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue('{"id":"foobar","uri":"http://backend-service:80","predicates":[{"name":"Path","args":{"arg0":"/test"}}]}')
      .exchange()
      .expectStatus().isCreated()
      .expectHeader().doesNotExist(HttpHeaders.LOCATION)
      .expectBody().isEmpty()

    cleanup:
    webTestClient
      .delete()
      .uri("/gateway/api/routes/v1/test-route-002")
      .exchange()
      .expectStatus().isOk()
  }

  def "should return 400 Bad Request when trying to create route with invalid format"() {
    expect:
    webTestClient
      .post()
      .uri("/gateway/api/routes/v1/test-route-005")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue('{"uri":"http://backend-service:80","predicates":[{"name":"Unknown"}]}')
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody().isEmpty()
  }
}
