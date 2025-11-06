package org.formation;

import java.util.logging.Logger;

import org.formation.jwt.JWTConfigurer;
import org.formation.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@Order(200)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	Logger logger = Logger.getLogger(RestSecurityConfig.class.getSimpleName() );
	
	@Autowired
	UserDetailsService userDetailsService;
	
	private final TokenProvider tokenProvider;

	@Autowired
	public RestSecurityConfig(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.antMatcher("/api/**").authorizeRequests()
	    .antMatchers("/api/authenticate").permitAll()
	    .anyRequest().authenticated()
	    .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .apply(securityConfigurerAdapter())
        .and()
        .csrf()
        .disable();

	}

	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	/**
	 * Not needed in SB 2.x. Il suffit d'avoir un UserDetailsService présent.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("Configuring DetailsService");
		auth.userDetailsService(userDetailsService);	
	}
	
	private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

}