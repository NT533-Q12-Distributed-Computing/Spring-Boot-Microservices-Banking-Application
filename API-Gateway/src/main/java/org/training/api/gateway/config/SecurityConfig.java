package org.training.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // ❌ Disable CSRF (gateway không cần CSRF)
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        // ✅ Authorization rules
        http.authorizeExchange(exchange -> exchange
            // ⭐ BẮT BUỘC cho Docker / Kubernetes
            .pathMatchers("/actuator/**").permitAll()

            // Cho phép đăng ký user không cần login
            .pathMatchers("/api/users/register").permitAll()

            // Các request khác phải auth
            .anyExchange().authenticated()
        );

        // ✅ OAuth2 login (browser redirect)
        http.oauth2Login();

        // ✅ OAuth2 Resource Server (JWT cho API)
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
