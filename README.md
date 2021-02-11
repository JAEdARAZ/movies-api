# movies-api
Search movies by name using the TMDB API

## Installation
```
git clone https://github.com/JAEdARAZ/movies-api.git
./mvnw spring-boot:run -Dspring-boot.run.arguments=--api.key=###
```


## Usage
get JWT with POST to: http://localhost:8080/api/authenticate

JSON body in the POST method:
```
{
    "username" : "admin",
    "password" : "admin123"
}
```

Add the JWT provided to the GET method header and try:


http://localhost:8080/api/movies?movieTitle=interestellar
