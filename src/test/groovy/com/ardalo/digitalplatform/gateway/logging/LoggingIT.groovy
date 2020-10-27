package com.ardalo.digitalplatform.gateway.logging

import org.junit.Rule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.OutputCaptureRule
import org.springframework.context.ApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.util.stream.Collectors

@SpringBootTest
@AutoConfigureWebTestClient
class LoggingIT extends Specification {

  @Autowired
  ApplicationContext applicationContext

  @Rule
  OutputCaptureRule outputCapture = new OutputCaptureRule()

  def "should write application logs in JSON format"() {
    when:
    LoggerFactory.getLogger("test-logger").warn("test message")
    def logMessage = getLastLineFromOutputCapture()

    then:
    logMessage.startsWith('{')
    logMessage.contains('"@timestamp":"')
    logMessage.contains('"type":"application"')
    logMessage.contains('"msg":"test message"')
    logMessage.contains('"logger":"test-logger"')
    logMessage.contains('"level":"WARN"')
    logMessage.contains('"class":"com.ardalo.digitalplatform.gateway.logging.LoggingIT"')
    logMessage.contains('"method":"')
    logMessage.contains('"file":"LoggingIT.groovy"')
    logMessage.contains('"line":')
    logMessage.endsWith('}')
  }

  def "should write access logs in JSON format"() {
    when:
    WebTestClient.bindToApplicationContext(applicationContext).build()
      .get()
      .uri("/gateway/alive?foo=bar")
      .exchange()
      .expectStatus().isOk()
    def logMessage = getLastLineFromOutputCapture()

    then:
    logMessage.startsWith('{')
    logMessage.contains('"@timestamp":')
    logMessage.contains('"type":"access"')
    logMessage.contains('"method":"GET"')
    logMessage.contains('"path":"/gateway/alive"')
    logMessage.contains('"query":"foo=bar"')
    logMessage.contains('"status":"200"')
    logMessage.contains('"duration":"')
    !logMessage.contains('"userAgent":')
    logMessage.matches(/.+"correlationId":"[a-z0-9-]{2,}".+/)
    !logMessage.contains('"remoteAddress":')
    logMessage.endsWith('}')
  }

  private getLastLineFromOutputCapture() {
    def logMessage = outputCapture.toString().trim()
    return logMessage.lines().collect(Collectors.toList()).last().trim()
  }
}
