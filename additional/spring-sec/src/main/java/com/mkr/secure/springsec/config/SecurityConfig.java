package com.mkr.secure.springsec.config;

import com.mkr.secure.springsec.filter.AuthTokenFilter;
import com.mkr.secure.springsec.utils.jwt.AuthEntryPointJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((req) ->
                        req
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/signin").permitAll()
                                .requestMatchers("/contact").permitAll()
//                                .requestMatchers("/admin").denyAll()
//                                .requestMatchers("/admin/**").denyAll()
                                .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .formLogin(Customizer.withDefaults()) // comment to disable the form login
                .exceptionHandling(
                        ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Disable CSRF for H2 console
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for H2 console
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var user = User.withUsername("user1")
//                .password(passwordEncoder().encode("user1"))
////                .password("{noop}user1")
//                .roles("USER")
//                .build();
//        var admin = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
////                .password("{noop}admin")
//                .roles("ADMIN")
//                .build();
//
//        // Use JdbcUserDetailsManager to store users in the database
//        // Ensure that the DataSource is properly configured in your application
//        // and that the necessary tables (like users, authorities) exist.
//        // Use schema.sql to create the necessary tables
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.createUser(user);
//        userDetailsManager.createUser(admin);
//        return userDetailsManager;
//
//        // Use InMemoryUserDetailsManager to store users in memory
////        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public CommandLineRunner initData(UserDetailsService userDetailsService) {
        return args -> {
//            JdbcUserDetailsManager userDetailsManager = (JdbcUserDetailsManager) userDetailsService;
            var user = User.withUsername("user1")
                .password(passwordEncoder().encode("user1"))
                .roles("USER")
                .build();
        var admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
