package com.mkr.secure.springsec.utils.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs = 300000; // 5 minutes
    @Value("${spring.app.jwtSecret}")
    private String jwtSecretKey;

    // Getting JWT from the request header
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        LOGGER.info("Authorization: {}", bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null; // No JWT found
    }

    // Generating Token from Username
    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        LOGGER.info("Generating token for user: {}", username);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    // Getting username from token
    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    // Generating Signing Key
    public SecretKey key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecretKey)
        );
    }
    // Validating Token
    public boolean validateJwtToken(String token) {
        try {
            LOGGER.info("Validate");
            Jwts.parser()
                    .verifyWith(key())
                    .build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims is empty: {}", e.getMessage());
        }
        return false;
    }
}
