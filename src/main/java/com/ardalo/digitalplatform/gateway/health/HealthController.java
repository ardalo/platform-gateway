package com.ardalo.digitalplatform.gateway.health;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class HealthController {

  @GetMapping("/alive")
  public ResponseEntity<Mono<Void>> isAlive() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/ready")
  public ResponseEntity<Mono<Void>> isReady() {
    return ResponseEntity.ok().build();
  }
}
