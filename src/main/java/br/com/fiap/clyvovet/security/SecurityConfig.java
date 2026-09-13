package br.com.fiap.clyvovet.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Rotas REST de negócio consumidas pelo app mobile, protegidas pelo JWT emitido em
    // /auth/login e /auth/register (ver JwtAuthenticationFilter).
    private static final String[] REST_API_PATTERNS = {
            "/tutores/**", "/pets/**", "/clinicas/**", "/veterinarios/**",
            "/consultas/**", "/vacinas/**", "/medicamentos/**", "/exames/**", "/alertas/**",
            "/usuarios/**",
    };

    private static final RequestMatcher REST_API_MATCHER = new OrRequestMatcher(
            Arrays.stream(REST_API_PATTERNS).map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new));

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Libera CORS para a API REST, permitindo que o app mobile (Expo Go / build)
    // e o Expo Web consigam consumir os endpoints a partir de outra origem.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/app/veterinario/**").hasRole("VETERINARIO")
                        .requestMatchers("/app/tutor/**").hasRole("TUTOR")
                        .requestMatchers("/app/**").authenticated()
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**")
                        .permitAll()
                        .requestMatchers(REST_API_PATTERNS).authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/app", false)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/app/acesso-negado")
                        // Sem isso, o entry point padrão do formLogin redirecionaria as chamadas
                        // REST sem token (o app mobile) para /login em vez de responder 401.
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                                REST_API_MATCHER))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // A API REST (usada via Swagger/Postman) segue sem token CSRF; o CSRF continua
                // ativo para os formulários da camada web em /app/**, que usam th:action.
                .csrf(csrf -> csrf.ignoringRequestMatchers(REST_API_MATCHER, new AntPathRequestMatcher("/auth/**")));

        return http.build();
    }
}
