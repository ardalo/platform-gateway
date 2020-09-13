package com.ardalo.digitalplatform.gateway.routing;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/routes/v1")
public class RouteController {

  private final GatewayControllerEndpoint gatewayControllerEndpoint;

  @Autowired
  public RouteController(GatewayControllerEndpoint gatewayControllerEndpoint) {
    this.gatewayControllerEndpoint = gatewayControllerEndpoint;
  }

  @GetMapping
  public Flux<Map<String, Object>> getRoutes() {
    return this.gatewayControllerEndpoint.routes();
  }

  @GetMapping("/{routeId}")
  public Mono<ResponseEntity<Map<String, Object>>> getRoute(@PathVariable String routeId) {
    return this.gatewayControllerEndpoint.route(routeId);
  }

  @PostMapping("/{routeId}")
  public Mono<ResponseEntity<Void>> saveRoute(@PathVariable String routeId, @RequestBody RouteDefinition route) {
    return this.gatewayControllerEndpoint
      .save(routeId, route)
      .doOnNext(response -> this.gatewayControllerEndpoint.refresh())
      .map(response -> ResponseEntity.status(response.getStatusCode()).build());
  }

  @DeleteMapping("/{routeId}")
  public Mono<ResponseEntity<Object>> deleteRoute(@PathVariable String routeId) {
    return this.gatewayControllerEndpoint
      .delete(routeId)
      .doOnNext(response -> this.gatewayControllerEndpoint.refresh());
  }
}
