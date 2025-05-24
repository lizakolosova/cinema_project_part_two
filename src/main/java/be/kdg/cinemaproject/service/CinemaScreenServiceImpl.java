package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.CinemaScreen;
import be.kdg.cinemaproject.repository.CinemaScreenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CinemaScreenServiceImpl implements CinemaScreenService {

    private final CinemaScreenRepository cinemaScreenRepository;

    public CinemaScreenServiceImpl(CinemaScreenRepository cinemaScreenRepository) {
        this.cinemaScreenRepository = cinemaScreenRepository;
    }

    @Override
    public List<CinemaScreen> findByType(String type) {
        return cinemaScreenRepository.findByScreenTypeContainingIgnoreCase(type);
    }

    @Override
    public CinemaScreen add(int screenNumber, String screenType, int size) {
        final CinemaScreen cinemaScreen = new CinemaScreen();
        cinemaScreen.setScreenNumber(screenNumber);
        cinemaScreen.setScreenType(screenType);
        cinemaScreen.setSize(size);
        return cinemaScreenRepository.save(cinemaScreen);
    }
}
