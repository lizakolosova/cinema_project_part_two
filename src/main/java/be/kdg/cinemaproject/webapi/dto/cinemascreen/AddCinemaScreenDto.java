package be.kdg.cinemaproject.webapi.dto.cinemascreen;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddCinemaScreenDto(@NotNull int screenNumber, @NotBlank String screenType, @NotNull int size) {
}
