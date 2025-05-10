package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.*;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.domain.exception.TicketNotFoundException;
import be.kdg.cinemaproject.repository.CinemaRepository;
import be.kdg.cinemaproject.repository.MovieRepository;
import be.kdg.cinemaproject.repository.TicketRepository;
import be.kdg.cinemaproject.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TicketServiceImplTest {

    @Autowired
    private TicketServiceImpl sut;

    @MockitoBean
    private TicketRepository ticketRepository;
    @MockitoBean
    private CinemaRepository cinemaRepository;
    @MockitoBean
    private MovieRepository movieRepository;
    @MockitoBean
    private VisitorRepository visitorRepository;


    @Test
    void ShouldDeleteTicket() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setPrice(14);
        Movie movie = new Movie();
        movie.setTitle("Movie");
        ticket.setMovie(movie);
        movie.setTickets(new ArrayList<>(List.of(ticket)));

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        sut.deleteById(1L);

        // Assert
        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void ShouldNotDeleteNotExistingTicket() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Executable action = () -> sut.deleteById(1L);

        // Assert
        assertThrows(TicketNotFoundException.class, action);
        verify(ticketRepository, never()).deleteById(any());
    }


    @Test
    void ShouldDeleteTicketWithoutMovie() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setPrice(14);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        sut.deleteById(1L);

        // Assert
        verify(ticketRepository).deleteById(1L);
    }


    @Test
    void ShouldAddTicket() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Movie");
        Cinema cinema = new Cinema();
        cinema.setAddress("Address");
        Visitor visitor = new Visitor();
        visitor.setName("Visitor");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(cinemaRepository.findById(1L)).thenReturn(Optional.of(cinema));
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(ticketRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        // Act
        Ticket ticket = sut.add(10.5, LocalDateTime.MAX, "IMAX", Availability.AVAILABLE, "image.jpg", 1L, 1L, 1L);

        // Assert
        assertEquals(10.5, ticket.getPrice());
        assertEquals("IMAX", ticket.getFormat());
        assertEquals(Availability.AVAILABLE, ticket.getAvailability());
        assertEquals("image.jpg", ticket.getImage());
        assertEquals(movie, ticket.getMovie());
        assertEquals(cinema, ticket.getCinema());
        assertEquals(visitor, ticket.getVisitor());
    }


    @Test
    void ShouldNotAddTicketWithoutMovie() {
        // Arrange
        Cinema cinema = new Cinema();
        cinema.setAddress("Address");
        Visitor visitor = new Visitor();
        visitor.setName("Visitor");

        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        when(cinemaRepository.findById(1L)).thenReturn(Optional.of(cinema));
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(ticketRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        // Act
        Executable action = () -> sut.add(10.5, LocalDateTime.MAX, "IMAX", Availability.AVAILABLE, "image.jpg", 1L, 1L, 1L);

        // Assert
        assertThrows(MovieNotFoundException.class, action);
        verify(ticketRepository, never()).deleteById(any());
    }

    @Test
    void ShouldNotAddTicketWithoutVisitor() {
        // Arrange
        Cinema cinema = new Cinema();
        cinema.setAddress("Address");
        Movie movie = new Movie();
        movie.setTitle("Movie");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(cinemaRepository.findById(1L)).thenReturn(Optional.of(cinema));
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());
        when(ticketRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        // Act
        Executable action = () -> sut.add(10.5, LocalDateTime.MAX, "IMAX", Availability.AVAILABLE, "image.jpg", 1L, 1L, 1L);

        // Assert
        assertThrows(NoSuchElementException.class, action);
        verify(ticketRepository, never()).deleteById(any());
    }

    @Test
    void ShouldNotAddTicketWithoutCinema() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Movie");
        Visitor visitor = new Visitor();
        visitor.setName("Visitor");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(cinemaRepository.findById(1L)).thenReturn(Optional.empty());
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(ticketRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

        // Act
        Executable action = () -> sut.add(10.5, LocalDateTime.MAX, "IMAX", Availability.AVAILABLE, "image.jpg", 1L, 1L, 1L);

        // Assert
        assertThrows(CinemaNotFoundException.class, action);
        verify(ticketRepository, never()).deleteById(any());
    }
}