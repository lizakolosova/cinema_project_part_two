# Programming 5
###### Name: Liza
###### Surname: Kolosova
###### KdG email: liza.kolosova@student.kdg.be
###### Student ID: 0167456-34
###### Academic year: 2024-2025
###### Group: ACS201
###### Domain entities: cinema screen (many) -(one) cinema (one) - (many) ticket (many) - (one) movie. Ticket is how I handle many-to-many relationship between movie and cinema so basically my initial domain was cinema screen - cinema - movie.
###### Build and Run Instructions:
###### You need Java 17+ and Gradle.
###### To build the project with gradle: ./gradlew clean build
###### To run the project with gradle: ./gradlew run
###### Additional info: use can access my page from http://localhost:8080/ only and then you will navigate through navbar.

# Week 2

## Searching for movies - OK
```http
GET http://localhost:8080/api/movies?title=Toy Story
Accept: application/json

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 18 Feb 2025 16:27:40 GMT

[
  {
    "id": 2,
    "title": "Toy Story 4",
    "releaseDate": "2019-06-21",
    "genre": "ANIMATION"
  }
]
Response file saved.
> 2025-02-18T172740.200.json

Response code: 200; Time: 24ms (24 ms); Content length: 79 bytes (79 B)
```
## Searching for movies - No content
```http
GET http://localhost:8080/api/movies?title=fckdfsnf
Accept: application/json

HTTP/1.1 204 
Date: Tue, 18 Feb 2025 16:28:14 GMT

<Response body is empty>

Response code: 204; Time: 20ms (20 ms); Content length: 0 bytes (0 B)
```
## Deleting one movie - No Content
```http
DELETE http://localhost:8080/api/movies/2

HTTP/1.1 204 
Date: Tue, 18 Feb 2025 16:28:32 GMT

<Response body is empty>

Response code: 204; Time: 139ms (139 ms); Content length: 0 bytes (0 B)
```
## Deleting one movie - Not Found
```http
DELETE http://localhost:8080/api/movies/9

HTTP/1.1 404 
Content-Length: 0
Date: Tue, 18 Feb 2025 16:28:53 GMT

<Response body is empty>

Response code: 404; Time: 25ms (25 ms); Content length: 0 bytes (0 B)
```
## Deleting one cinema - No content

```http
DELETE http://localhost:8080/api/cinemas/1

HTTP/1.1 204
Date: Wed, 19 Feb 2025 19:34:39 GMT

<Response body is empty>

Response code: 204; Time: 91ms (91 ms); Content length: 0 bytes (0 B)
```

## Deleting one cinema - Not Found

```http
DELETE http://localhost:8080/api/cinemas/9

HTTP/1.1 404 
Content-Length: 0
Date: Wed, 19 Feb 2025 19:36:49 GMT

<Response body is empty>

Response code: 404; Time: 18ms (18 ms); Content length: 0 bytes (0 B)
```


