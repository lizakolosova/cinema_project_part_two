package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.CinemaScreen;
import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
import be.kdg.cinemaproject.repository.CinemaRepository;
import be.kdg.cinemaproject.repository.CinemaScreenRepository;
import be.kdg.cinemaproject.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaScreenRepository screenRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository, CinemaScreenRepository screenRepository, TicketRepository ticketRepository) {
        this.cinemaRepository = cinemaRepository;
        this.screenRepository = screenRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        log.debug("Retrieved {} cinemas from the repository.", cinemas.size());
        return cinemas;
    }

    public Cinema findById(Long cinemaId) {
        return cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema with ID " + cinemaId + " not found.", 404, "Cinema not found."));
    }

    @Override
    public void saveCinema(Cinema cinema) {
        log.debug("Attempting to save cinema: {}", cinema.getName());
        if (cinemaRepository.findByName(cinema.getName()).isPresent()) {
            log.warn("Cinema with the name '{}' already exists. Skipping save.", cinema.getName());
            return;
        }
        cinemaRepository.save(cinema);
        log.info("Cinema '{}' saved successfully.", cinema.getName());
    }


    @Override
    public void deleteById(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema with ID " + id + " not found.", 404, "Cinema not found"));

        for (CinemaScreen screen: new ArrayList<>(cinema.getScreens())) {
            screen.setCinema(null);
            screenRepository.save(screen);
        }
        for (Ticket ticket: new ArrayList<>(cinema.getTickets())) {
            ticket.setCinema(null);
            ticketRepository.save(ticket);
        }
        cinemaRepository.deleteById(id);
        log.info("Cinema with ID {} deleted successfully.", id);
    }

    @Override
    public List<Cinema> getCinemasByCapacity(int minCapacity) {
        return cinemaRepository.findCinemaByCapacityAfter(minCapacity);
    }

    @Override
    public Cinema findByName(String name) {
        return cinemaRepository.findByName(name)
                .orElse(null);
    }

    @Override
    public List<Cinema> findCinemasByAddress(String address) {
        return cinemaRepository.findByAddress(address);
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        Cinema cinema = cinemaRepository.findByIdWithScreens(id);
        if (cinema == null) {
            throw new CinemaNotFoundException("Cinema not found. Id:" + id, 404, "Not found");
        }
        return cinema;
    }
}


