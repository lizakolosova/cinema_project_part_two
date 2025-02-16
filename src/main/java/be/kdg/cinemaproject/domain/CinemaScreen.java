package be.kdg.cinemaproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "cinema_screens")
public class CinemaScreen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "screen_number")
    private int screenNumber;

    @Column(name = "screen_type")
    private String screenType;

    @Column(name = "size")
    private int size;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cinema cinema;
    
    public CinemaScreen(int screenNumber, String screenType, int size) {
        this.screenNumber = screenNumber;
        this.screenType = screenType;
        this.size = size;
        this.cinema = new Cinema();
    }

    @Override
    public String toString() {
        return "Cinema Screen " + screenNumber + " - Type: " + screenType + ", Size: " + size;
    }

}
