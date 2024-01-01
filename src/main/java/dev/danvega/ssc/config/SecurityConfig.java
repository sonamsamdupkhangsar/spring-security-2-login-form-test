package dev.danvega.ssc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .securityMatcher("/special/**")
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
              //  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //.httpBasic(withDefaults())
                //.formLogin((withDefaults()))
                .formLogin(httpSecurityFormLoginConfigurer ->
                        {
                            try {
                                httpSecurityFormLoginConfigurer.
                                        loginPage("/special/login")
                                        .defaultSuccessUrl("/special/home")
                                        .permitAll()
                                        .and()
                                        .logout(httpSecurityLogoutConfigurer ->
                                                httpSecurityLogoutConfigurer.logoutUrl("/special/logout")
                                                        .permitAll()
                                                        //.deleteCookies("JSESSIONID")
                                        );
                            } catch (Exception e) {
                                LOG.error("error occured in order 1 security config", e);
                            }
                        }
                )
                .csrf().disable()
                .authenticationManager(authenticationManager1())
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll();
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .headers(headers -> headers.frameOptions().disable())
                .build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/").permitAll();
                        auth.requestMatchers("/error").permitAll();
                        auth.requestMatchers("/css/**").permitAll();
                        auth.requestMatchers("/special/**").permitAll();
                        auth.anyRequest().authenticated();
                    }
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                        {
                            try {
                                httpSecurityFormLoginConfigurer.loginPage("/regular/login")
                                        .defaultSuccessUrl("/regular/home")
                                        .permitAll()
                                        .and()
                                        .logout(httpSecurityLogoutConfigurer ->
                                                httpSecurityLogoutConfigurer.logoutUrl("/regular/logout")
                                                        .permitAll()
                                                       // .deleteCookies("JSESSIONID")
                                        );
                            } catch (Exception e) {
                                LOG.error("exception occured in order 3 security config", e);
                            }
                        }

                )

                .csrf().disable()
                .authenticationManager(authenticationManager2())
                .build();
    }



    private AuthenticationManager authenticationManager1() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService1());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    private AuthenticationManager authenticationManager2() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService2());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    UserDetailsService userDetailsService1() {
        var user = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    UserDetailsService userDetailsService2() {
        var user = User.withDefaultPasswordEncoder()
                .username("user2")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
