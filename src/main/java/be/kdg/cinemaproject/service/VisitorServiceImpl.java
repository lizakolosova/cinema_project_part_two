package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Role;
import be.kdg.cinemaproject.domain.Visitor;
import be.kdg.cinemaproject.repository.VisitorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
@Slf4j
public class VisitorServiceImpl implements VisitorService {

    private final VisitorRepository visitorRepository;

    private final PasswordEncoder passwordEncoder;

    public VisitorServiceImpl(VisitorRepository visitorRepository, PasswordEncoder passwordEncoder) {
        this.visitorRepository = visitorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Visitor add(String name, String email, String password, Role role) {
        final Visitor visitor = new Visitor();
        visitor.setName(name);
        visitor.setEmail(email);
        visitor.setPassword(passwordEncoder.encode(password));
        visitor.setRole(role);
        log.info("Admin added: {}", visitor.getEmail());
        return visitorRepository.save(visitor);
    }
}
