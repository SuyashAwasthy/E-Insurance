package com.techlabs.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techlabs.app.security.JwtAuthenticationEntryPoint;
import com.techlabs.app.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors()
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/E-Insurance/auth/**").permitAll()
                .requestMatchers("/E-Insurance/admin/**").permitAll()
                .requestMatchers("/E-Insurance/employee/**").permitAll()
                .requestMatchers("/E-Insurance/customer/**").permitAll()
                .requestMatchers("/E-Insurance/agent/**").permitAll()
                
                .requestMatchers("/E-Insurance/toall/**").permitAll()
                .requestMatchers("/E-Insurance/admin/{planId}").permitAll()
                .requestMatchers("/E-Insurance/admin/getall/document-types").permitAll()                
                .requestMatchers(HttpMethod.GET, "/E-Insurance/file/").permitAll()
                .requestMatchers(HttpMethod.POST, "/E-Insurance/file/").permitAll()
                .requestMatchers(HttpMethod.GET, "/E-Insurance/file/view/{name}").permitAll()
                .requestMatchers(HttpMethod.POST, "/E-Insurance/customer/test").permitAll()
                .requestMatchers(HttpMethod.POST, "/E-Insurance/customer/ create-payment-intent").permitAll()
                .requestMatchers(HttpMethod.POST, "/E-Insurance/customer/{customerId}/buyWithoutAgent").permitAll()
                .requestMatchers(HttpMethod.GET, "/E-Insurance/toall/ payment-tax").permitAll()
                .requestMatchers(HttpMethod.POST,"/E-Insurance/customer/{customerId}/buy-policy").permitAll()
                .requestMatchers(HttpMethod.POST,"/E-Insurance/customer/${customerId}/buyWithoutAgent").permitAll()
                .requestMatchers(HttpMethod.PUT,"/E-Insurance/employee/verify/{customerId}").permitAll()
                .requestMatchers(HttpMethod.GET,"/E-Insurance/customer/policy/{policyId}/claim-status").permitAll()
                
                .requestMatchers("/swagger-ui/**", "/v3/api-docs").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }
}
