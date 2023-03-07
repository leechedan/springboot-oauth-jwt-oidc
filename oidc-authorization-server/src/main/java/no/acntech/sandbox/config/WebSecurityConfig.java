package no.acntech.sandbox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests().antMatchers("/uaa/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("john.doe@accenture.com")
                .password("abcd1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
