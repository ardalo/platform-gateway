package com.ardalo.digitalplatform.gateway.logging

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class RequestIdFilterIT extends Specification {

  @Autowired
  ApplicationContext applicationContext

  WebTestClient webTestClient
  MockWebServer mockWebServer

  def setup() {
    this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build()
    this.mockWebServer = new MockWebServer()
    this.mockWebServer.start()
    this.mockWebServer.enqueue(new MockResponse())
  }

  def cleanup() {
    this.mockWebServer.shutdown()
  }

  def "should add X-Request-ID header to downstream requests"() {
    given: "an existing platform route"
    webTestClient
      .post()
      .uri("/gateway/api/routes/v1/request-id-test")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue('{"uri":"http://' + this.mockWebServer.hostName + ':' + this.mockWebServer.port + '","predicates":[{"name":"Path","args":{"arg0":"/request-id-test"}}]}')
      .exchange()
      .expectStatus().isCreated()

    when: "existing platform route is called"
    webTestClient
      .get()
      .uri("/request-id-test")
      .exchange()
      .expectStatus().isOk()

    then: "routed request contains X-Request-ID request header"
    def recordedRequest = mockWebServer.takeRequest(50, TimeUnit.MILLISECONDS)
    recordedRequest.method == HttpMethod.GET.toString()
    recordedRequest.path == "/request-id-test"
    recordedRequest.getHeader("X-Request-ID").matches('^[a-f0-9-]+$')
  }
}
