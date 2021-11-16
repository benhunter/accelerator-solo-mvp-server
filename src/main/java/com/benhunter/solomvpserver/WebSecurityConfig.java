package com.benhunter.solomvpserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        UserDetails alice = userBuilder
                .username("alice")
                .password("wonderland")
                .roles("USER")
                .build();
        UserDetails bob = userBuilder
                .username("bob")
                .password("thebuilder")
                .roles("USER")
                .build();
        UserDetails admin = userBuilder
                .username("admin")
                .password("nimda")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(alice, bob, admin);
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

//                .httpBasic(Customizer.withDefaults())
                .httpBasic().and()
//                .formLogin(Customizer.withDefaults())  // TODO do I need the formLogin?
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



/*
    // Setup for persistent users.
    @Autowired
    public DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
//        auth.userDetailsService(jdbcUserDetailsManager()).passwordEncoder(passwordEncoder());  // old

        auth.jdbcAuthentication()
                .dataSource(this.dataSource)
//                .withDefaultSchema()
                .withUser(
                        User.withUsername("bob")
//                                .passwordEncoder()
                                .password("thebuilder")
                                .roles("USER"));

    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager()
    {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);

        User.UserBuilder userBuilder = User.builder();
        UserDetails alice = userBuilder
                .username("alice")
//                .password("wonderland")
                .password("{bcrypt}$2a$10$LJ1cheyBg/pn8f0uNYUhce4BI/BwZr4XLvz6t3fy1K8Bl/buzud8O")
                // {bcrypt}$2a$10$LJ1cheyBg/pn8f0uNYUhce4BI/BwZr4XLvz6t3fy1K8Bl/buzud8O
                .roles("USER")
                .build();
        if (!jdbcUserDetailsManager.userExists(alice.getUsername())) {
            jdbcUserDetailsManager.createUser(alice);
        }

        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();  // No hash, insecure
    }
*/
}
