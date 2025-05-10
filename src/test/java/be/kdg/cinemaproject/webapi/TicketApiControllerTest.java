package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.Availability;
import be.kdg.cinemaproject.domain.Role;
import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.security.CustomUserDetails;
import be.kdg.cinemaproject.service.AuthorizationService;
import be.kdg.cinemaproject.service.TicketService;
import be.kdg.cinemaproject.webapi.dto.ticket.PatchTicketDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.junit.jupiter.api.function.Executable;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TicketApiControllerTest {

    @Autowired
    private TicketApiController sut;
    @MockitoBean
    private TicketService ticketService;
    @MockitoBean
    private AuthorizationService authorizationService;


    @Test
    void shouldPatchTicketAsAdmin() {
        // Arrange
        final Ticket ticket = new Ticket();
        final PatchTicketDto dto = new PatchTicketDto(14, LocalDateTime.MAX, Availability.AVAILABLE);
        CustomUserDetails userDetails = new CustomUserDetails("username", "password", 1L, Role.ADMINISTRATOR);
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(ticketService.patch(1L,14, LocalDateTime.MAX, Availability.AVAILABLE))
                .thenAnswer(answer ->{
                    ticket.setPrice(answer.getArgument(1));
                    ticket.setShowtime(answer.getArgument(2));
                    ticket.setAvailability(answer.getArgument(3));
                    return ticket;
                });
        when(authorizationService.canModifyTicket(any(), eq(1L))).thenReturn(true);

        // Act
        final var response = sut.patch(1L, dto);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(14, response.getBody().price());
        verify(ticketService).patch(1L,14, LocalDateTime.MAX, Availability.AVAILABLE);
    }

    @Test
    void shouldNotPatchTicketWhenTicketIsNotAssignedToVisitor() {
        // Arrange
        final Ticket ticket = new Ticket();
        final PatchTicketDto dto = new PatchTicketDto(14, LocalDateTime.MAX, Availability.AVAILABLE);
        CustomUserDetails userDetails = new CustomUserDetails("username", "password", 1L, Role.VISITOR);
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(ticketService.patch(1L,14, LocalDateTime.MAX, Availability.AVAILABLE))
                .thenAnswer(answer ->{
                    ticket.setPrice(answer.getArgument(1));
                    ticket.setShowtime(answer.getArgument(2));
                    ticket.setAvailability(answer.getArgument(3));
                    return ticket;
                });
        when(authorizationService.canModifyTicket(any(), eq(1L))).thenReturn(false);

        // Act
        Executable action = () -> sut.patch(1L, dto);

        // Assert
        assertThrows(AccessDeniedException.class, action);
    }

    @Test
    void shouldAllowVisitorToPatchAssignedTicket() {

        // Arrange
        final Ticket ticket = new Ticket();
        final PatchTicketDto dto = new PatchTicketDto(14, LocalDateTime.MAX, Availability.AVAILABLE);
        CustomUserDetails userDetails = new CustomUserDetails("visitor", "password", 2L, Role.VISITOR);
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(ticketService.patch(1L,14, LocalDateTime.MAX, Availability.AVAILABLE))
                .thenAnswer(answer ->{
                    ticket.setPrice(answer.getArgument(1));
                    ticket.setShowtime(answer.getArgument(2));
                    ticket.setAvailability(answer.getArgument(3));
                    return ticket;
                });
        when(authorizationService.canModifyTicket(any(), eq(1L))).thenReturn(true);

        // Act
        var response = sut.patch(1L, dto);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(14, response.getBody().price());
        verify(ticketService).patch(1L,14, LocalDateTime.MAX, Availability.AVAILABLE);
    }

}