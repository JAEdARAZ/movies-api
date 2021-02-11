package com.jaedaraz.moviesapi.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaedaraz.moviesapi.entity.Movie;
import com.jaedaraz.moviesapi.entity.ResponseTMDB;
import com.jaedaraz.moviesapi.models.AuthenticationRequest;
import com.jaedaraz.moviesapi.models.AuthenticationResponse;
import com.jaedaraz.moviesapi.services.MyUserDetailsService;
import com.jaedaraz.moviesapi.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class MovieRestController {

	@Value("${api.key}")
	private String apiKey;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//JWT
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	
	@Cacheable("movieTitle")
	@GetMapping("/movies")
	public List<Movie> getMovieByName(@RequestParam("movieTitle") String movieTitle) {
		String responseJson = webClientBuilder.build()
				.get()
				.uri("https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + movieTitle)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		
		try {
			ResponseTMDB responseTMDB = objectMapper.readValue(responseJson, ResponseTMDB.class);
			List<Movie> movies = responseTMDB.getMovies();
			return movies;
		}
		catch(JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//POST porque recibe las credenciales en el body
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
			authenticationManager.authenticate( //lo que haria spring por defecto
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch(BadCredentialsException e){
			throw new Exception ("Credenciales incorrectas", e);
		}
		
		//recuperar UserDetails para generar el JWT
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		//generar el token y retornarlo usando el objeto creado (AuthenticationResponse)
		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
