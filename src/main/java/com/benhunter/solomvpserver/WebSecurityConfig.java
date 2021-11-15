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

    // Single HttpSecurity config
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
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
