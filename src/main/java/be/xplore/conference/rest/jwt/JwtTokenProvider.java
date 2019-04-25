package be.xplore.conference.rest.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final AuthenticationProvider authenticationProvider;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expirationInMillis}")
    private long jwtExpirationInMs;

    @Autowired
    public JwtTokenProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    private String generateTokenFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateToken(user.getUsername(), jwtExpirationInMs);
    }

    public String getAdminNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String authenticateAndGenerateToken(String adminName, String password) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(adminName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return generateTokenFromAuthentication(authentication);
    }

    public String generateToken(String adminNameOrEmail, long expirationInMillis) {
        return Jwts.builder()
                .setSubject(adminNameOrEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationInMillis))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
