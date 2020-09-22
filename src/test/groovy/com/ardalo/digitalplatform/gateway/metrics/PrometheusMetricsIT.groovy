package com.ardalo.digitalplatform.gateway.metrics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest
@AutoConfigureWebTestClient
class PrometheusMetricsIT extends Specification {

  @Autowired
  ApplicationContext applicationContext

  def "should expose prometheus metrics endpoint"() {
    expect:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri("/gateway/prometheus-metrics")
      .exchange()
      .expectStatus().isOk()
      .expectBody(String).consumeWith({ res ->
        assert res.responseBody.contains("jvm_memory_max_bytes")
      })
  }
}
