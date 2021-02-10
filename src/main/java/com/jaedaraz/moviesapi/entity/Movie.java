package com.jaedaraz.moviesapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

	@JsonProperty("title")
	private String title;
	
	@JsonProperty("overview")
	private String overview;
	
	@JsonProperty("homepage")
	private String homepage;
	
	@JsonProperty("release_date")
	private String releaseDate;
	
	
	public Movie() {
		
	}

	public Movie(String title, String overview, String homepage, String releaseDate) {
		this.title = title;
		this.overview = overview;
		this.homepage = homepage;
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
}
