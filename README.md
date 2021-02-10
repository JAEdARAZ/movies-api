# movies-api
Search movies by name using the TMDB API

git clone https://github.com/JAEdARAZ/movies-api.git

Crear el archivo application.properties en (/src/main/resources/application.properties)
incluir: api.key=###

./mvnw package
java -jar app.jar

user:password - admin:admin123

ejemplo:
http://localhost:8080/api/movies?movieTitle=interestellar
