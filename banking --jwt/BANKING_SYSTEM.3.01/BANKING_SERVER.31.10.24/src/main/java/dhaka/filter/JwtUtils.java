package dhaka.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final String secret_key = "doit,this-is-a-very-secure-512-bit-secret-key-for-jwt-token-generation-it-should-be-long-enough!";
    private final long expiration_time = 86400000;

    private final Key key = Keys.hmacShaKeyFor(secret_key.getBytes());

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", role) // Add roles claim
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration_time))
                .signWith(key)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
/*
    parseClaimsJwt(): This is used for parsing JWTs that are not signed.
    parseClaimsJws(): This is used for parsing signed JWTs like the one you're generating with SignatureAlgorithm.HS512.
*/

    private boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            if (isTokenExpired(token)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

