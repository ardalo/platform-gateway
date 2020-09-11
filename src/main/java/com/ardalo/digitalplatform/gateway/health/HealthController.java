package com.ardalo.digitalplatform.gateway.health;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class HealthController {

  @GetMapping("/alive")
  public Mono<ResponseEntity<Void>> isAlive() {
    return Mono.just(ResponseEntity.ok().build());
  }

  @GetMapping("/ready")
  public Mono<ResponseEntity<Void>> isReady() {
    return Mono.just(ResponseEntity.ok().build());
  }
}
