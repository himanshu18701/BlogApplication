package com.himanshu.blog_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT email, CONCAT('{noop}',password) ,'true' FROM user WHERE email=?");
        manager.setAuthoritiesByUsernameQuery("SELECT email, CONCAT('ROLE_',role) FROM user WHERE email=?");
        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/api/posts/**","/api/comments/**","/posts","/registerForm","/registerNewUser").permitAll()
                                .requestMatchers("/posts/new", "/posts/save", "/posts/updatePost", "/posts/deletePost", "/comments/delete").hasAnyRole("AUTHOR", "ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.loginPage("/loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(successHandler())
                                .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    private AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/posts");
        return handler;
    }
}
