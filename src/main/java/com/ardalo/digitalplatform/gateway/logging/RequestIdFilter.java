package com.ardalo.digitalplatform.gateway.logging;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestIdFilter implements GlobalFilter {

  private static final String REQUEST_ID_HEADER_NAME = "X-Request-ID";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    exchange.getRequest()
      .mutate()
      .header(REQUEST_ID_HEADER_NAME, exchange.getRequest().getId());
    return chain.filter(exchange);
  }
}
