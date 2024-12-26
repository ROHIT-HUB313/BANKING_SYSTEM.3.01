package dhaka.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final UserDetails userDetails;
    private final String credentials;

    public JwtAuthenticationToken(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        this.credentials = null;  // As no credentials are needed with JWT
        setAuthenticated(true);
    }

    public JwtAuthenticationToken(String token) {
        super(null);  // No authorities yet
        this.credentials = token;
        this.userDetails = null;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}

