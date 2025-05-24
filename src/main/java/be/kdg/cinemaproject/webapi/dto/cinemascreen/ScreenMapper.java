package be.kdg.cinemaproject.webapi.dto.cinemascreen;

import be.kdg.cinemaproject.domain.CinemaScreen;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ScreenMapper {
    CinemaScreenDto toCinemaScreenDto(CinemaScreen cinemaScreen);
}
