package be.kdg.cinemaproject.webapi.dto.cinemascreen;

import be.kdg.cinemaproject.domain.CinemaScreen;

public record CinemaScreenDto(Long id, int screenNumber, String screenType, int size) {
    public static CinemaScreenDto fromCinemaScreen(final CinemaScreen screen) {
        return new CinemaScreenDto(screen.getId(), screen.getScreenNumber(), screen.getScreenType(), screen.getSize());
    }
}
