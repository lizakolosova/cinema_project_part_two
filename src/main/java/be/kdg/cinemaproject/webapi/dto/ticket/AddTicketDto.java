package be.kdg.cinemaproject.webapi.dto.ticket;

import be.kdg.cinemaproject.domain.Availability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AddTicketDto(@NotNull Double price, @NotNull LocalDateTime showtime, @NotBlank String format, @NotBlank String image,@NotNull Availability availability,@NotNull Long movieId,@NotNull Long cinemaId) {
}
