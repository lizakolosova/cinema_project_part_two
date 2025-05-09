package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.CinemaScreen;

import java.util.List;


public record CinemaWithScreensViewModel(Long id, String name, int capacity, String address, String image, List<CinemaScreen> screens) {
    public static CinemaWithScreensViewModel from(final Cinema cinema) {
        return new CinemaWithScreensViewModel(cinema.getId(), cinema.getName(), cinema.getCapacity(), cinema.getAddress(), cinema.getImage(), cinema.getScreens());
    }
}
