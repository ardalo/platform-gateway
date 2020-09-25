package com.ardalo.digitalplatform.gateway.correlationid;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter {

  private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-ID";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    exchange.getRequest()
      .mutate()
      .header(CORRELATION_ID_HEADER_NAME, exchange.getRequest().getId());
    return chain.filter(exchange);
  }
}
