package be.kdg.cinemaproject.webapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketDto(Long id, @NotNull double price, @NotBlank LocalDateTime showtime, @NotBlank String format, String availability) {

}
