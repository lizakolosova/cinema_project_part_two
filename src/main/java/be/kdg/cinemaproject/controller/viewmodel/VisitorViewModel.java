package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Visitor;

public record VisitorViewModel(Long id, String name, String email) {
    public static VisitorViewModel from(final Visitor visitor) {
        return new VisitorViewModel(visitor.getId(), visitor.getName(), visitor.getEmail());
    }
}
