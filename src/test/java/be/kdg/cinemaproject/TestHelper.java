package be.kdg.cinemaproject;

import be.kdg.cinemaproject.domain.*;
import be.kdg.cinemaproject.repository.CinemaRepository;
import be.kdg.cinemaproject.repository.MovieRepository;
import be.kdg.cinemaproject.repository.TicketRepository;
import be.kdg.cinemaproject.repository.VisitorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TestHelper {

    public static final String ADMIN_EMAIL = "admin@gmail.com";

    public static final String VISITOR_EMAIL = "lzkolosova@gmail.com";
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void cleanUp() {
        ticketRepository.deleteAll();
        movieRepository.deleteAll();
        cinemaRepository.deleteAll();
        visitorRepository.deleteAll();
    }

    public List<Ticket> findTickets() {
        return ticketRepository.findAll();
    }


    public Visitor createVisitor(){
        final Visitor visitor = new Visitor();
        visitor.setEmail(VISITOR_EMAIL);
        visitor.setRole(Role.VISITOR);
        visitor.setPassword(passwordEncoder.encode("test"));
        visitorRepository.save(visitor);
        return visitor;
    }

    @Transactional
    public Ticket addTicketToVisitor(String email) {
        final Ticket ticket = new Ticket();
        ticket.setFormat("3D");
        ticket.setPrice(11);
        ticketRepository.save(ticket);
        ticket.setVisitor(visitorRepository.getVisitorsByEmail(email));
        return ticket;
    }


    public Visitor createAdmin(){
        final Visitor visitor = new Visitor();
        visitor.setEmail(ADMIN_EMAIL);
        visitor.setRole(Role.ADMINISTRATOR);
        visitor.setPassword(passwordEncoder.encode("test"));
        visitorRepository.save(visitor);
        return visitor;
    }

    public Movie createMovie(){
        final Movie movie = new Movie();
        movie.setTitle("Movie");
        movie.setGenre(Genre.ACTION);
        movie.setRating(9.9);
        movie.setReleaseDate(LocalDate.parse("2025-11-02"));
        movie.setImage("image.jpg");
        movieRepository.save(movie);
        return movie;
    }

    public Movie createMovieWithTitleAndRating(String title, double rating){
        final Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(Genre.ACTION);
        movie.setRating(rating);
        movie.setReleaseDate(LocalDate.parse("2025-11-02"));
        movie.setImage("image.jpg");
        movieRepository.save(movie);
        return movie;
    }

    public Ticket createTicket(){
        final Ticket ticket = new Ticket();
        ticket.setFormat("3D");
        ticket.setPrice(11);
        ticketRepository.save(ticket);
        return ticket;
    }

    public Cinema createCinema(){
        final Cinema cinema = new Cinema();
        cinema.setName("Cinema");
        cinemaRepository.save(cinema);
        return cinema;
    }
}