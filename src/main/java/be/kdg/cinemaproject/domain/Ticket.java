package be.kdg.cinemaproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "show_time")
    private LocalDateTime showtime;

    @Column(name = "price")
    private double price;

    @Column(name = "format")
    private String format;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;


    @ManyToOne(fetch = FetchType.LAZY)
    private Cinema cinema;
}
