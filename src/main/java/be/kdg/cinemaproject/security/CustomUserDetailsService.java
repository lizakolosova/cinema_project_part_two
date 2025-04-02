package be.kdg.cinemaproject.security;

import be.kdg.cinemaproject.repository.VisitorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final VisitorRepository visitorRepository;

    public CustomUserDetailsService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return visitorRepository
                .findByEmail(username)
                .map(visitor -> new CustomUserDetails(
                        visitor.getEmail(),
                        visitor.getPassword(),
                        visitor.getId(),
                        visitor.getRole()
                ))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
