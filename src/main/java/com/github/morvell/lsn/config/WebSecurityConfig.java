package com.github.morvell.lsn.config;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.repo.UserDetailsRepository;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/js/**", "/error**").permitAll().anyRequest()
                .authenticated().and().logout().logoutSuccessUrl("/").permitAll().and()
                .csrf().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepository userDetailsRepository) {

        return map -> {
            var id = String.valueOf(map.get("sub"));
            var user = userDetailsRepository.findById(id).orElseGet(() -> {
                var newUser = new User();

                newUser.setId(id);
                newUser.setName((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setGender((String) map.get("gender"));
                newUser.setLocale((String) map.get("local"));
                newUser.setUserpic((String) map.get("picture"));
                return newUser;
            });

            user.setLastVisit(LocalDateTime.now());

            return userDetailsRepository.save(user);
        };
    }
}
