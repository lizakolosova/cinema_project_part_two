package be.kdg.cinemaproject.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    public CustomUserDetails(final String username,
                             final String password,
                             final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
