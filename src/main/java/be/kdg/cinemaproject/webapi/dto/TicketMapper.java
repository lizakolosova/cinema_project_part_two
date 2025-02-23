package be.kdg.cinemaproject.webapi.dto;

import be.kdg.cinemaproject.domain.Ticket;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface TicketMapper {
    TicketDto toTicketDto(Ticket ticket);
    Ticket toTicket(TicketDto ticketDto);
}
