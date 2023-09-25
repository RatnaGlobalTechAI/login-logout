package com.rgt.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.rgt.enums.ApiPathExclusion;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private RestTokenProvider restTokenProvider;
	
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((authorize) -> {
            try {
				authorize.antMatchers(List.of(ApiPathExclusion.values()).stream().map(apiPath -> apiPath.getPath())
				            .toArray(String[]::new)).permitAll()
				.anyRequest().authenticated().and().apply(new JwtConfigurer(restTokenProvider));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }).httpBasic(Customizer.withDefaults());
		
		
		http
	    .logout((logout) -> logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()));
		return http.build();
		
		
	}

	
	@Bean
	UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder()
				.username("user")
				.password(passwordEncoder().encode("user"))
				.roles("ADMIN").build();
		
		return new InMemoryUserDetailsManager(userDetails);
				
	}

}
