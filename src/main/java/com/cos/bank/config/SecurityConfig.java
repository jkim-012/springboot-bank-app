package com.cos.bank.config;

import com.cos.bank.config.jwt.JwtAuthenticationFilter;
import com.cos.bank.config.jwt.JwtAuthorizationFilter;
import com.cos.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("DEBUG: BCryptPasswordEncoder is created");
        return new BCryptPasswordEncoder();
    }

    // Jwt filter
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager)); // authentication check
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager)); // authorization check
            super.configure(builder);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.debug("DEBUG: SecurityFilterChain is created");

        http
                .headers().frameOptions().disable()

                .and()
                .csrf().disable()
                .cors().configurationSource(configurationSource())

                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/s/**", "/api/account/**", "/api/accounts/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        // apply jwt filter
        http.apply(new CustomSecurityFilterManager());

        // security exception handling entry point - intercept authentication exceptions
        http.exceptionHandling().authenticationEntryPoint(
                (request, response, authException) -> {
                    CustomResponseUtil.noAuthentication(response, "Please Login.");
                });

        // security exception handling entry point - intercept authorization exceptions
        http.exceptionHandling().accessDeniedHandler(
                (request, response, accessDeniedException) -> {
                    CustomResponseUtil.noAuthorization(response, "You have no authorization.");
                });

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {

        log.debug("DEBUG: CorsConfigurationSource is created");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
