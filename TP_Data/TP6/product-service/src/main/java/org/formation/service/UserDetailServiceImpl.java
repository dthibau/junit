package org.formation.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	public final static String COMMA_DELIMITER=";";
	
	private Map<String,UserDto> allUsers = new HashMap<>();
	@PostConstruct
	public void loadCsv() throws IOException, Exception {
		Resource userResource = new ClassPathResource("users.csv", this.getClass().getClassLoader());
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(userResource.getInputStream()))) {
		    String line;
		    while ((line = br.readLine()) != null) {		    	
		        String[] values = line.split(COMMA_DELIMITER);
		        UserDto uDto = new UserDto(values[0],values[1]);
		        allUsers.put(values[0], uDto);
		    }
		}
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = allUsers.get(username);
        if ( userDto == null ) 
        	throw new UsernameNotFoundException("Invalides login");
        Set<GrantedAuthority> grantedAuthorities= new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(userDto.getLogin(), "{noop}"+userDto.getPassword(), grantedAuthorities);
	}

}
