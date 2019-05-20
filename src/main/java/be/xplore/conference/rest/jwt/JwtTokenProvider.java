package be.xplore.conference.rest.jwt;

import be.xplore.conference.rest.jwt.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final AuthenticationProvider authenticationProvider;
    private final JwtProperties jwtProperties;

    @Autowired
    public JwtTokenProvider(AuthenticationProvider authenticationProvider,
                            JwtProperties jwtProperties) {
        this.authenticationProvider = authenticationProvider;
        this.jwtProperties = jwtProperties;
    }

    private String generateTokenFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateToken(user.getUsername(), jwtProperties.getExpirationInMillis());
    }

    public String getAdminNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException |
                ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException e) {
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    public String authenticateAndGenerateToken(String adminName, String password) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(adminName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return generateTokenFromAuthentication(authentication);
    }

    private String generateToken(String adminNameOrEmail, long expirationInMillis) {
        return Jwts.builder()
                .setSubject(adminNameOrEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationInMillis))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }
}
