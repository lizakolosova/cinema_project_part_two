package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.TestHelper;
import be.kdg.cinemaproject.domain.*;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.domain.exception.TicketNotFoundException;
import be.kdg.cinemaproject.repository.CinemaRepository;
import be.kdg.cinemaproject.repository.MovieRepository;
import be.kdg.cinemaproject.repository.VisitorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TicketServiceImplTest {
    @Autowired
    private TicketServiceImpl sut;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private TestHelper testHelper;

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldAddTicket() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);

        double price = 10.0;
        LocalDateTime showtime = LocalDateTime.now().plusDays(1);
        String format = "IMAX";
        Availability availability = Availability.AVAILABLE;
        String image = "test_image.jpg";

        // Act
        Ticket ticket = sut.add(price, showtime, format, availability, image, movie.getId(), cinema.getId(), visitor.getId());

        // Assert
        assertNotNull(ticket);
        assertEquals(price, ticket.getPrice());
        assertEquals(showtime, ticket.getShowtime());
        assertEquals(format, ticket.getFormat());
        assertEquals(availability, ticket.getAvailability());
        assertEquals(movie.getId(), ticket.getMovie().getId());
        assertEquals(cinema.getId(), ticket.getCinema().getId());
        assertEquals(visitor.getId(), ticket.getVisitor().getId());
    }

    @Test
    void shouldThrowExceptionWhenMovieNotFound() {
        // Arrange
        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);

        Cinema finalCinema = cinema;
        Visitor finalVisitor = visitor;

        // Act
        Executable action = () -> sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "IMAX", Availability.AVAILABLE, "VERY_good_image.jpg", 999L, finalCinema.getId(), finalVisitor.getId());
        // Assert
        assertThrows(MovieNotFoundException.class, action);
    }

    @Test
    void shouldThrowExceptionWhenCinemaNotFound() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);
        Movie finalMovie = movie;
        Visitor finalVisitor = visitor;

        // Act
        Executable action = () -> sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "IMAX", Availability.AVAILABLE, "VERY_good_image.jpg", finalMovie.getId(), 999L, finalVisitor.getId());

        // Assert
        assertThrows(CinemaNotFoundException.class, action);
    }

    @Test
    void shouldThrowExceptionWhenVisitorNotFound() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);
        Movie finalMovie = movie;
        Cinema finalCinema = cinema;

        // Act
        Executable action = () -> sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "IMAX", Availability.AVAILABLE, "VERY_good_image.jpg", finalMovie.getId(), finalCinema.getId(), 999L);

        // Assert
        assertThrows(NoSuchElementException.class, action);
    }
    @Test
    void shouldUpdateOnlyPrice() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);

        Ticket ticket = sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "Something", Availability.AVAILABLE, "VERY_good_image.jpg", movie.getId(), cinema.getId(), visitor.getId());

        // Act
        Ticket updatedTicket = sut.patch(ticket.getId(), 15.99, null, null);

        // Assert
        assertNotNull(updatedTicket);
        assertEquals(15.99, updatedTicket.getPrice());
        assertEquals(LocalDateTime.of(2025, 4, 5, 12, 0, 0), updatedTicket.getShowtime());
        assertEquals(Availability.AVAILABLE, updatedTicket.getAvailability());
    }

    @Test
    void shouldUpdateOnlyShowtime() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);

        Ticket ticket = sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "Something", Availability.AVAILABLE, "VERY_good_image.jpg", movie.getId(), cinema.getId(), visitor.getId());

        // Act
        Ticket updatedTicket = sut.patch(ticket.getId(), 0, LocalDateTime.of(2025, 4, 6, 12, 0, 0), null);

        // Assert
        assertNotNull(updatedTicket);
        assertEquals(LocalDateTime.of(2025, 4, 6, 12, 0, 0), updatedTicket.getShowtime());
        assertEquals(10.0, updatedTicket.getPrice());
        assertEquals(Availability.AVAILABLE, updatedTicket.getAvailability());
    }

    @Test
    void shouldUpdateOnlyAvailability() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);

        Ticket ticket = sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "Something", Availability.AVAILABLE, "VERY_good_image.jpg", movie.getId(), cinema.getId(), visitor.getId());

        // Act
        Ticket updatedTicket = sut.patch(ticket.getId(), 0, null, Availability.SOLD);

        // Assert
        assertNotNull(updatedTicket);
        assertEquals(LocalDateTime.of(2025, 4, 5, 12, 0, 0), updatedTicket.getShowtime());
        assertEquals(Availability.SOLD, updatedTicket.getAvailability());
    }

    @Test
    void shouldPatchPriceShowDateAndAvailability() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie = movieRepository.save(movie);

        Cinema cinema = new Cinema();
        cinema.setName("Test Cinema");
        cinema = cinemaRepository.save(cinema);

        Visitor visitor = new Visitor();
        visitor.setName("Test Worker");
        visitor.setRole(Role.VISITOR);
        visitor = visitorRepository.save(visitor);

        Ticket ticket = sut.add(10.0, LocalDateTime.of(2025, 4, 5, 12, 0, 0), "Something", Availability.AVAILABLE, "VERY_good_image.jpg", movie.getId(), cinema.getId(), visitor.getId());

        // Act
        Ticket updatedTicket = sut.patch(ticket.getId(), 12.50, LocalDateTime.of(2025, 4, 7, 12, 0, 0), Availability.RESERVED);

        // Assert
        assertNotNull(updatedTicket);
        assertEquals(12.50, updatedTicket.getPrice());
        assertEquals(LocalDateTime.of(2025, 4, 7, 12, 0, 0), updatedTicket.getShowtime());
        assertEquals(Availability.RESERVED, updatedTicket.getAvailability());
    }

    @Test
    void shouldNotPatchTicket() {
        // Arrange
        long nonExistentTicketId = 999L;

        // Act
        Executable action = () -> sut.patch(nonExistentTicketId, 10.0, LocalDateTime.now(), Availability.AVAILABLE);

        // Assert
        assertThrows(TicketNotFoundException.class, action);
    }
}
