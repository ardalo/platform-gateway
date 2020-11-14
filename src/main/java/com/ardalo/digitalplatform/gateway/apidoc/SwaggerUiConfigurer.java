package com.ardalo.digitalplatform.gateway.apidoc;

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
      .GET("/gateway/apidoc", req -> ServerResponse.permanentRedirect(URI.create("/gateway/apidoc/swagger-ui/index.html")).build())
      .build();
  }
}
