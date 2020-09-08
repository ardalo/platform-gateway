package com.ardalo.digitalplatform.gateway.swagger;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SwaggerUiConfigurer {

  @Bean
  public RouterFunction<ServerResponse> swaggerUiOnRootPathRedirect() {
    return route()
      .GET("/", req -> ServerResponse.permanentRedirect(URI.create("/swagger-ui/index.html")).build())
      .build();
  }
}
