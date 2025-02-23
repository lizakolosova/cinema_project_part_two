package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.service.TicketService;
import be.kdg.cinemaproject.webapi.dto.AddTicketDto;
import be.kdg.cinemaproject.webapi.dto.PatchTicketDto;
import be.kdg.cinemaproject.webapi.dto.TicketDto;
import be.kdg.cinemaproject.webapi.dto.TicketMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public TicketApiController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @PostMapping
    public ResponseEntity<TicketDto> add(@RequestBody @Valid final AddTicketDto addTicketDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket data");
        }
        final Ticket ticket = ticketService.add(addTicketDto.price(), addTicketDto.showtime(), addTicketDto.format(), addTicketDto.availability(), addTicketDto.image());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketMapper.toTicketDto(ticket));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketDto> patch(@PathVariable("id") final Long id,
                                           @RequestBody @Valid final PatchTicketDto patchTicketDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket data");
        }
        final Ticket ticket =
                ticketService.patch(id, patchTicketDto.price(), patchTicketDto.showtime(), patchTicketDto.availability());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ticketMapper.toTicketDto(ticket));
    }
}
