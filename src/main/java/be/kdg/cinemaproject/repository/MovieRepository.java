package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.Genre;
import be.kdg.cinemaproject.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitle(String title);

    List<Movie> findByReleaseDateAfter(LocalDate releaseDate);

    @Query("""
            FROM Movie m
            LEFT JOIN FETCH m.tickets ss
            LEFT JOIN FETCH ss.cinema
            WHERE m.id = :id
""")
    Movie findByIdWithSessionsAndCinemas(@Param("id") Long id);

    @Query("SELECT m FROM Movie m WHERE (:genre IS NULL OR m.genre = :genre) AND (:rating IS NULL OR m.rating >= :rating)")
    List<Movie> findMoviesByGenreAndRating(Genre genre, Double rating);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
