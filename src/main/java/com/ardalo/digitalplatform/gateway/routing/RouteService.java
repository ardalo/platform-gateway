package com.ardalo.digitalplatform.gateway.routing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RouteService {

  private final RouteRepository routeRepository;

  @Autowired
  public RouteService(RouteRepository routeRepository) {
    this.routeRepository = routeRepository;
  }

  public Flux<Route> findAll() {
    return this.routeRepository.findAll();
  }

  public Mono<Route> createRoute(Route route) {
    return this.routeRepository.createRoute(route);
  }
}
