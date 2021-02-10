package com.jaedaraz.moviesapi.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MovieRestController {

	@GetMapping("/movies/{movieTitle}")
	public String getMovieByName(@PathVariable String movieTitle) {
		return movieTitle;
	}
	
}
