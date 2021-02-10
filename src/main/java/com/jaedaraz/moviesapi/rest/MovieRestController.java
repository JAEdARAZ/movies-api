package com.jaedaraz.moviesapi.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaedaraz.moviesapi.entity.Movie;
import com.jaedaraz.moviesapi.entity.ResponseTMDB;

@RestController
@RequestMapping("/api")
public class MovieRestController {

	@Value("${api.key}")
	private String apiKey;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping("/movies/{movieTitle}")
	public List<Movie> getMovieByName(@PathVariable String movieTitle) {
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
}
