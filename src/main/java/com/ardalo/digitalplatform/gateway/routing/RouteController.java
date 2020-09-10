package com.ardalo.digitalplatform.gateway.routing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/routes/v1")
public class RouteController {

  private final RouteService routeService;

  @Autowired
  public RouteController(RouteService routeService) {
    this.routeService = routeService;
  }

  @GetMapping
  public Flux<Route> getRoutes() {
    return this.routeService.findAll();
  }

  @PostMapping
  public ResponseEntity<Mono<Route>> createRoute(@RequestBody Route route) {
    Mono<Route> createdRoute = this.routeService.createRoute(route);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRoute);
  }
}
