package be.kdg.cinemaproject.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "rating")
    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    public Movie(Long id, String title, LocalDate releaseDate, double rating, Genre genre, String image) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }

    public Movie(String title, LocalDate releaseDate, double rating, Genre genre, String image) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}