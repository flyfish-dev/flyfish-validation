package org.springframework.http.server.reactive;
import org.springframework.http.HttpMethod;
public interface ServerHttpRequest { RequestPath getPath(); HttpMethod getMethod(); }
