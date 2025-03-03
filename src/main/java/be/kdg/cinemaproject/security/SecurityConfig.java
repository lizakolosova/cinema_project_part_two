package be.kdg.cinemaproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/home").permitAll()
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/cinemas"),
                                antMatcher(HttpMethod.GET, "/movies"),
                                antMatcher(HttpMethod.GET, "/tickets"),
                                antMatcher(HttpMethod.GET, "/movies/find"),
                                regexMatcher(HttpMethod.GET, "/cinemas/details/\\d+"),
                                regexMatcher(HttpMethod.GET, "/tickets/details/\\d+"),
                                regexMatcher(HttpMethod.GET, "/movies/details/\\d+")).permitAll()
                        .requestMatchers(
                                antMatcher("/js/**"),
                                antMatcher("/css/**"),
                                antMatcher("/icons/**"),
                                antMatcher("/images/**"),
                                antMatcher("/webjars/**")).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api")) {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            } else {
                                response.sendRedirect("/login");
                            }
                        }))
                .csrf(AbstractHttpConfigurer::disable) // we will enable this again later
                .formLogin(login -> login.loginPage("/login").permitAll())
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
