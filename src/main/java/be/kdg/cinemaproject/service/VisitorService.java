package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Role;
import be.kdg.cinemaproject.domain.Visitor;
import org.springframework.stereotype.Service;

@Service
public interface VisitorService {
    Visitor add(String name, String email, String password, Role role);
}
