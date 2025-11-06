package org.formation.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${appli.secretKey}")
    private String secretKey;          // recommandé: base64
    @Value("${appli.validity}")
    private String validity;           // en secondes
    @Value("${appli.rememberMe}")
    private String remeberMe;          // en secondes

    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;
    private Key signingKey;

    @PostConstruct
    public void init() {
        // Durées
        this.tokenValidityInMilliseconds = 1000L * Integer.parseInt(validity);
        this.tokenValidityInMillisecondsForRememberMe = 1000L * Integer.parseInt(remeberMe);

        // Clé HMAC
        // Si la propriété est en Base64, on décode. Sinon on tombe en UTF-8 (compat).
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            // secret non base64 -> fallback UTF-8 (assure-toi d'avoir au moins 32 chars pour HS256/HS512)
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date expiry = new Date(now + (Boolean.TRUE.equals(rememberMe)
                ? this.tokenValidityInMillisecondsForRememberMe
                : this.tokenValidityInMilliseconds));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(this.signingKey, SignatureAlgorithm.HS512)
                .setExpiration(expiry)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(this.signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY, String.class).split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(this.signingKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            // couvre SignatureException, MalformedJwtException, UnsupportedJwtException, etc.
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e.getMessage());
        }
        return false;
    }

    // Getters/Setters si nécessaire pour tests ou override
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getValidity() { return validity; }
    public void setValidity(String validity) { this.validity = validity; }
    public String getRemeberMe() { return remeberMe; }
    public void setRemeberMe(String remeberMe) { this.remeberMe = remeberMe; }
}
