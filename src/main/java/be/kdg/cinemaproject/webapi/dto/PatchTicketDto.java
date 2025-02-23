package be.kdg.cinemaproject.webapi.dto;

import be.kdg.cinemaproject.domain.Availability;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PatchTicketDto(@DecimalMin(value = "3.0")
                             @DecimalMax(value = "15.0") double price, @FutureOrPresent LocalDateTime showtime, Availability availability) {
}
