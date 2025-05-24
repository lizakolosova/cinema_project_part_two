package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.CinemaScreen;

import java.util.List;

public interface CinemaScreenService {
    List<CinemaScreen> findByType(String type);
    CinemaScreen add(int screenNumber, String screenType, int size);

}
