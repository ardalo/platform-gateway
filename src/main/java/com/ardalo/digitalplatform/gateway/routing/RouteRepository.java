package com.ardalo.digitalplatform.gateway.routing;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RouteRepository {

  private final List<Route> routes = new ArrayList<>();

  public Flux<Route> findAll() {
    return Flux.fromIterable(this.routes);
  }

  public Mono<Route> createRoute(Route route) {
    Route createdRoute = new Route();
    createdRoute.id = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(route.destinationUrl);
    createdRoute.destinationUrl = route.destinationUrl;
    this.routes.add(createdRoute);
    return Mono.just(createdRoute);
  }
}
