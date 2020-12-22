package com.ardalo.digitalplatform.gateway;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlatformGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformGatewayApplication.class, args);
	}

  /**
   * Copy of {@link org.springframework.cloud.gateway.config.GatewayAutoConfiguration.GatewayActuatorConfiguration#gatewayControllerEndpoint(List, List, List, RouteDefinitionWriter, RouteLocator, RouteDefinitionLocator)} gatewayControllerEndpoint}.
   *
   * Necessary because this bean is not being initialized via auto configuration as the
   * Spring Cloud Gateway Actuator Endpoint is disabled. This is done to have full control
   * of the endpoints exposed by this Gateway.
   */
  @Bean
  public GatewayControllerEndpoint gatewayControllerEndpoint(
    List<GlobalFilter> globalFilters,
    List<GatewayFilterFactory> gatewayFilters,
    List<RoutePredicateFactory> routePredicates,
    RouteDefinitionWriter routeDefinitionWriter,
    RouteLocator routeLocator,
    RouteDefinitionLocator routeDefinitionLocator) {
    return new GatewayControllerEndpoint(
      globalFilters, gatewayFilters, routePredicates, routeDefinitionWriter, routeLocator, routeDefinitionLocator);
  }
}
