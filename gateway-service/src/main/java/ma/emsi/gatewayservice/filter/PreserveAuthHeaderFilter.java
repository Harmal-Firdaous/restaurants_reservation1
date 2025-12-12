package ma.emsi.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PreserveAuthHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Clone the request while preserving the Authorization header
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .build();

        // Build a new exchange with the updated request
        ServerWebExchange newExchange = exchange.mutate()
                .request(request)
                .build();

        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
