package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.Ticket;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CinemaRepositoryTest {
    @Autowired
    private CinemaRepository sut;
    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
        sut.deleteAll();
    }

    @Test
    void ShouldDeleteCinemaWithoutReferenceToTicket() {
        // Arrange
        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = sut.save(cinema);

        // Act
        sut.deleteById(cinema.getId());

        // Assert
        Optional<Cinema> deletedCinema = sut.findById(cinema.getId());
        assertFalse(deletedCinema.isPresent());
    }

    @Test
    void ShouldNotAllowDeleteCinemaWithReferenceToTicket() {
        // Arrange
        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = sut.save(cinema);

        Ticket ticket = new Ticket();
        ticket.setCinema(cinema);
        ticketRepository.save(ticket);
        Cinema finalCinema = cinema;

        // Act
        final Executable action = () -> sut.deleteById(finalCinema.getId());

        // Assert
        assertThrows(DataIntegrityViolationException.class, action);
    }

    @Test
    void ShouldNotThrowExceptionWhenDeletingNonExistingCinema() {
        // Arrange
        Long nonExistingCinemaId = 999L;

        // Act
        final Executable action = () -> sut.deleteById(nonExistingCinemaId);

        // Assert
        assertDoesNotThrow(action);
    }

    @Test
    void ShouldNotAllowDuplicateCinemaNames() {
        // Arrange
        Cinema cinema1 = new Cinema();
        cinema1.setName("Unique Cinema");
        sut.save(cinema1);

        Cinema cinema2 = new Cinema();
        cinema2.setName("Unique Cinema");
        // Act
        final Executable action = () -> sut.save(cinema2);

        // Assert
        assertThrows(DataIntegrityViolationException.class, action);
    }

    @Test
    void ShouldNotAllowToSaveCinemaWithoutName() {
        // Arrange
        Cinema cinema1 = new Cinema();
        cinema1.setAddress("Some Address");

        // Act
        final Executable action = () -> sut.save(cinema1);

        // Assert
        assertThrows(DataIntegrityViolationException.class, action);
    }

    @Test
    void ShouldLoadTicketsLazily() {
        // Arrange
        Cinema cinema = new Cinema();
        cinema.setName("Lazy Loaded Cinema");
        cinema = sut.save(cinema);

        Ticket ticket = new Ticket();
        ticket.setCinema(cinema);
        ticketRepository.save(ticket);

        // Act
        Cinema fetchedCinema = sut.findById(cinema.getId()).orElseThrow();

        // Assert
        assertThrows(LazyInitializationException.class, () -> fetchedCinema.getTickets().size());
    }

}