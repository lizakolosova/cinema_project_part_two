package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Role;
import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.domain.exception.TicketNotFoundException;
import be.kdg.cinemaproject.repository.TicketRepository;
import be.kdg.cinemaproject.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class AuthorizationService {
    private final TicketRepository ticketRepository;

    public AuthorizationService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public boolean canModifyTicket(final CustomUserDetails user, final Long ticketId) {
        return canModifyTicket(
                user,
                ticketRepository.findByIdWithVisitor(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"))
        );
    }

    public boolean canModifyTicket(final CustomUserDetails user, Ticket ticket) {
        log.debug("Checking if user is null");
        if (user == null) {
            log.debug("User is null");
            return false;
        }
        log.debug("User is not null");
        log.debug("Checking if user is administrator");
        if (user.getRole() == Role.ADMINISTRATOR) {
            log.debug("User is administrator {}", user.getRole());
            return true;
        }
        log.debug("User is not administrator {}", user.getRole());
        boolean isTicketOwner = ticket.getVisitor() != null && ticket.getVisitor().getId().equals(user.getVisitorId());
        log.debug("State of ownership: {}", isTicketOwner);
        return isTicketOwner;
    }
}
