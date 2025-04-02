package be.kdg.cinemaproject.security;

import be.kdg.cinemaproject.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails extends User {
    private final Long visitorId;
    private final Role role;

    public CustomUserDetails(final String username,
                             final String password,
                             final Long visitorId,
                             final Role role) {
        super(username, password, List.of(new SimpleGrantedAuthority("ROLE_" + role.toString())));
        this.visitorId = visitorId;
        this.role = role;
    }
}
