package com.benhunter.solomvpserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Single HttpSecurity config to allow all requests.
    // CSRF is enabled by default.
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().permitAll();
//    }

    // Single HttpSecurity config to allow all requests.
    // Explicitly disabling CSRF protection.
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests().anyRequest().permitAll()
//                .and()
//                .csrf().disable()
//        ;
//    }

    // Single HttpSecurity config to allow OPTION and require authentication everywhere else.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Manual method to allow CORS Preflight checks from web browsers.
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/api/alarms/**").permitAll()
//                .and()

                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()

                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())  // TODO do I need the formLogin?
                .cors().and()
                .csrf().disable();
    }

    /*
//    @Configuration
//    @Order(1)
//    public static class ApiOptionsWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests((requests) -> {
//                        requests
//                                // Allow OPTIONS without auth
//                                // TODO try with / at end. Do I need /** to apply to child paths?
//                                .antMatchers(HttpMethod.OPTIONS, "/api/alarms").permitAll()
//                                .anyRequest().authenticated();
//                    }
//            );
//        }
//    }
     */


    /*
//    @Configuration
//    @Order(2)
//    public static class AllowAuthenticatedWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests((requests) -> requests.anyRequest().permitAll());  // Allow any request.

//            http.authorizeRequests((requests) -> requests
            // Allow only authenticated requests.
//                            .anyRequest().authenticated())
//                    .httpBasic(Customizer.withDefaults())
//                    .formLogin(Customizer.withDefaults());
//        }

//    }
     */

}
