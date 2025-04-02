package be.kdg.cinemaproject.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cinemas")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;


    @Column(name = "capacity")
    private int capacity;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "cinema",  fetch = FetchType.LAZY)
    private List<CinemaScreen> screens;

    @OneToMany(mappedBy = "cinema",fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    public Cinema(Long id, String name, String address, int capacity, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
        this.screens = new ArrayList<>();
        this.tickets = new ArrayList<>();
    }
    public Cinema(String name, String address, int capacity, String image) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
        this.screens = new ArrayList<>();
        this.tickets = new ArrayList<>();
    }


    public void addScreens(CinemaScreen screen) {
        this.screens.add(screen);
    }

    @Override
    public String toString() {
        return "Cinema: " + name + ", Address: " + address + ", Capacity: " + capacity;
    }
}
