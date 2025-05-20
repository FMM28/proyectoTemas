package mx.unam.aragon.elZorro.config;

import mx.unam.aragon.elZorro.service.user.EmpleadoDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final EmpleadoDetailsService empleadoDetailsService;

    public SecurityConfig(EmpleadoDetailsService empleadoDetailsService) {
        this.empleadoDetailsService = empleadoDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login?invalid-session=true")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?expired=true")
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .requestCache(cache -> cache
                        .requestCache(requestCache())
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(empleadoDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream()
                    .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/administracion");
                return;
            }
            if (authentication.getAuthorities().stream()
                    .anyMatch(granted -> granted.getAuthority().equals("ROLE_CAJA"))) {
                response.sendRedirect("/caja");
                return;
            }
            response.sendRedirect("/");
        };
    }

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || auth instanceof AnonymousAuthenticationToken) {
                response.sendRedirect("/login");
                return;
            }

            String referer = request.getHeader("Referer");

            HttpSession session = request.getSession();
            session.setAttribute("DENIED_URL", request.getRequestURI());

            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer);
            } else {
                if (auth.getAuthorities().stream()
                        .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"))) {
                    response.sendRedirect("/administracion");
                } else if (auth.getAuthorities().stream()
                        .anyMatch(granted -> granted.getAuthority().equals("ROLE_CAJA"))) {
                    response.sendRedirect("/caja");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }
}