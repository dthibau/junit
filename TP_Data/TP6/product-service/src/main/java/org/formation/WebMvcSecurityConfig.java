package org.formation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class WebMvcSecurityConfig extends WebSecurityConfigurerAdapter {

	Logger logger = LoggerFactory.getLogger(WebMvcSecurityConfig.class);

	@Autowired
	UserDetailsService userDetailService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatcher(new RegexRequestMatcher("^((?!api).)*$", null)).csrf().disable().authorizeRequests().anyRequest()
				.authenticated().and().formLogin().and().logout().logoutUrl("/logout").invalidateHttpSession(true)
				.permitAll();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("Configuring DetailsService");
		auth.userDetailsService(userDetailService);
	}

}
