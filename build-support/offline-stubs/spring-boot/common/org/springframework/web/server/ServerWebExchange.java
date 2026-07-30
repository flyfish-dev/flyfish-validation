package org.springframework.web.server;
import org.springframework.http.server.reactive.ServerHttpRequest;
public interface ServerWebExchange { ServerHttpRequest getRequest(); }
