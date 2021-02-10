package com.jaedaraz.moviesapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api")
public class MovieRestController {

	@Value("${api.key}")
	private String apiKey;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@GetMapping("/movies/{movieTitle}")
	public String getMovieByName(@PathVariable String movieTitle) {
		String responseJson = webClientBuilder.build()
				.get()
				.uri("https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + movieTitle)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		
		return responseJson;
	}
}
