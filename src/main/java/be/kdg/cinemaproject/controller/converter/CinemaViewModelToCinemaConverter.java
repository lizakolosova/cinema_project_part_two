package be.kdg.cinemaproject.controller.converter;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.controller.viewmodel.CinemaViewModelForForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CinemaViewModelToCinemaConverter implements Converter<CinemaViewModelForForm, Cinema> {

    @Override
    public Cinema convert(CinemaViewModelForForm source) {
        Cinema cinema = new Cinema();
        cinema.setName(source.getName());
        cinema.setAddress(source.getAddress());
        cinema.setCapacity(source.getCapacity());
        cinema.setImage(source.getImage());
        log.debug("CinemaModelView is converted to Cinema.");
        return cinema;
    }
}
