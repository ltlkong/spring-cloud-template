package com.ltech.uaa.util;

import com.ltech.uaa.model.UserPrincipal;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    public String generateTokenWithExpirationAndSubject(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return generateToken(expiryDate,userPrincipal.getId());
    }

    public String generateToken(Date expiryDate, String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            // Invalid JWT signature
            logger.error(ex);
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            logger.error(ex);
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            logger.error(ex);
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            logger.error(ex);
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            logger.error(ex);
        }
        return false;
    }
}
