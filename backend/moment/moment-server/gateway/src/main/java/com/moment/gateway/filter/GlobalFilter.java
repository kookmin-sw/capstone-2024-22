package com.moment.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter(){super(Config.class);}
    @Override
    public GatewayFilter apply(Config config){
        return(exchange, chain) ->{
            ServerHttpRequest request =  exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
//            log.info("global filter baseMessage: {}",config.getBaseMessage());
            if (config.isPreLogger()){
                log.info("Global Filter Start: request id->{}",request.getLocalAddress());
                log.info("Global Filter Start: request path->{}",request.getURI());
                log.info("Global Filter Start: request Body->{}",request.getBody());

            }
            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                if(config.isPreLogger()){
                    log.info("global filter End: response code->{}", response.getStatusCode());
                }
            }));
        };
    }
    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}
