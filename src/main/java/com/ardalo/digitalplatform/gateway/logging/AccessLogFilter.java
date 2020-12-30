package com.ardalo.digitalplatform.gateway.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.logstash.logback.composite.loggingevent.LoggingEventFormattedTimestampJsonProvider;
import net.logstash.logback.composite.loggingevent.MdcJsonProvider;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessLogFilter implements WebFilter {

  private final Logger accessLogger = createAccessLogger();

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    long startTime = System.currentTimeMillis();
    ServerHttpRequest originalRequest = exchange.getRequest();

    return chain.filter(exchange)
      .doFinally(signalType -> {
        Map<String, String> mdcParameters = new HashMap<>();
        mdcParameters.put("type", "access");
        mdcParameters.put("method", originalRequest.getMethodValue());
        mdcParameters.put("path", originalRequest.getPath().value());
        mdcParameters.put("query", originalRequest.getURI().getQuery());
        mdcParameters.put("status", String.valueOf(exchange.getResponse().getRawStatusCode()));
        mdcParameters.put("duration", String.valueOf(System.currentTimeMillis() - startTime));
        mdcParameters.put("userAgent", originalRequest.getHeaders().getFirst("User-Agent"));
        mdcParameters.put("requestId", originalRequest.getId());
        mdcParameters.put("remoteAddress", Optional.ofNullable(originalRequest.getRemoteAddress())
          .map(InetSocketAddress::getHostName)
          .orElse(null));

        if (this.requestRoutedToDownstreamService(exchange)) {
          mdcParameters.put("matchedRoute", Optional.ofNullable(exchange.<Route>getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR))
            .map(Route::getId)
            .orElse(null));
          mdcParameters.put("forwardedTo", Optional.ofNullable(exchange.<URI>getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR))
            .map(URI::toString)
            .orElse(null));
        }

        mdcParameters.forEach(MDC::put);
        accessLogger.info("access log");
        mdcParameters.forEach((key, value) -> MDC.remove(key));
      });
  }

  private Logger createAccessLogger() {
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    LoggingEventCompositeJsonEncoder logEncoder = new LoggingEventCompositeJsonEncoder();
    logEncoder.setContext(loggerContext);
    logEncoder.getProviders().addProvider(new LoggingEventFormattedTimestampJsonProvider());
    logEncoder.getProviders().addProvider(new MdcJsonProvider());
    logEncoder.start();

    ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
    logConsoleAppender.setContext(loggerContext);
    logConsoleAppender.setName("CONSOLE_JSON");
    logConsoleAppender.setEncoder(logEncoder);
    logConsoleAppender.start();

    ch.qos.logback.classic.Logger logger = loggerContext.getLogger(AccessLogFilter.class);
    logger.setAdditive(false);
    logger.setLevel(Level.INFO);
    logger.addAppender(logConsoleAppender);
    return logger;
  }

  private boolean requestRoutedToDownstreamService(ServerWebExchange exchange) {
    return Boolean.TRUE.equals(exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ALREADY_ROUTED_ATTR));
  }
}
