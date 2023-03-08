package no.acntech.sandbox.config;

import no.acntech.sandbox.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@EnableConfigurationProperties({AuthProperties.class})
public class WebSecurityConfig {

    @Autowired
    AuthProperties authProperties;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf().disable().httpBasic().disable()
                .authorizeRequests().requestMatchers(getRequestMatcherArr()).permitAll()
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

    public RequestMatcher[] getRequestMatcherArr() {
        return authProperties.getWhitelists().stream().map(i->new AntPathRequestMatcher(i)).toArray(size -> new RequestMatcher[size]);
    }
}
