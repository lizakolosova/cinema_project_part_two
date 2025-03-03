package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Cinema;

public record CinemaViewModel(Long id, String name, int capacity, String address, String image) {
    public static CinemaViewModel from(final Cinema cinema) {
        return new CinemaViewModel(cinema.getId(), cinema.getName(), cinema.getCapacity(), cinema.getAddress(), cinema.getImage());
    }
}