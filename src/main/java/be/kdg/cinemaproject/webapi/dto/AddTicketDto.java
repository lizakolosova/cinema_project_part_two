package be.kdg.cinemaproject.webapi.dto;

import be.kdg.cinemaproject.domain.Availability;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AddTicketDto(@NotNull double price, @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime showtime, @NotBlank String format, @NotBlank String image, Availability availability) {
}
