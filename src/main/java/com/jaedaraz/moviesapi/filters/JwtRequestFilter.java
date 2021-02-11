package com.jaedaraz.moviesapi.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jaedaraz.moviesapi.services.MyUserDetailsService;
import com.jaedaraz.moviesapi.util.JwtUtil;

//filtro para buscar en el Header el JWT
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7); //quitar 'Bearer '
			username = jwtUtil.extractUsername(jwt); //extraer nombre usuario del JWT
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
			
			//si el jwt no ha caducado y el nombre de usuario es correcto
			if(jwtUtil.validateToken(jwt, userDetails)){
				//acciones que haria Spring Security por defecto. Ahora solo se hace si el JWT es correcto
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						 new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				 
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext( ).setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		//pasa control al siguiente filtro de la cadena
		filterChain.doFilter(request, response);
	}

}
