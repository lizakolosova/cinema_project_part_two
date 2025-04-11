package be.kdg.cinemaproject.webapi.dto.visitor;

import be.kdg.cinemaproject.domain.Visitor;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface VisitorMapper {
    VisitorDto toVisitorDto(Visitor visitor);
    Visitor toVisitor(VisitorDto visitorDto);
}

