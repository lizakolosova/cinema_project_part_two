package be.kdg.cinemaproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity security) throws Exception {
        return security
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/visitors", "/api/screens")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/home").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/visitors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/screens").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/screens").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/screens").permitAll()
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/cinemas"),
                                antMatcher(HttpMethod.GET, "/movies"),
                                antMatcher(HttpMethod.GET, "/tickets"),
                                antMatcher(HttpMethod.GET, "/movies/find"),
                                regexMatcher(HttpMethod.GET, "/cinemas/details/\\d+"),
                                regexMatcher(HttpMethod.GET, "/tickets/details/\\d+"),
                                regexMatcher(HttpMethod.GET, "/movies/details/\\d+")).permitAll()
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/cinemas/addcinema"),
                                antMatcher(HttpMethod.GET, "/movies/addmovie"),
                                antMatcher(HttpMethod.DELETE, "/api/cinemas/\\d+"),
                                antMatcher(HttpMethod.DELETE, "/api/movies/\\d+")).hasAnyRole("ADMINISTRATOR")
                        .requestMatchers(
                                antMatcher("/js/**"),
                                antMatcher("/js/util/**"),
                                antMatcher("/css/**"),
                                antMatcher("/icons/**"),
                                antMatcher("/images/**"),
                                antMatcher("/webjars/**")).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api")) {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"Unauthorized\"}");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )
                .formLogin(login -> login.loginPage("/login").permitAll())
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
