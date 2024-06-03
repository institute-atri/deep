package org.instituteatri.deep.infrastructure.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class DefendantConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.GET, "/v1/defendant").permitAll().requestMatchers(HttpMethod.POST,
                            "/v1/defendant/create").permitAll());
        }
}
