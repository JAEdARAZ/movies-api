package com.jaedaraz.moviesapi.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jaedaraz.moviesapi.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private Filter jwtRequestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(myUserDetailsService);
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .cors()
	        .and()
        	.csrf().disable() //ya que se utiliza JWT
	        .authorizeRequests()
	        .antMatchers("/api/authenticate").permitAll() //abrir endpoint para autenticación - JWT
        	.anyRequest().authenticated()
        	.and().sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //no se crea, ahora se utiliza JWT
        
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); //uso del filtro creado lo primero
        	
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	

	@Override
	@Bean //necesario para ser utilizado en el controlador (JWT)
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
}
