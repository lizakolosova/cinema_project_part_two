package be.kdg.cinemaproject.webapi.dto.visitor;

import be.kdg.cinemaproject.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VisitorDto(Long id, @NotBlank String name, @NotBlank String email, @NotBlank String password, @NotNull Role role) {
}
