package com.liviavilaca.investimentsmanager.util.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Claims getClaimsFromToken(String token) throws AuthenticationException {
        Claims claims = null;
        try {
            claims =  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims =  null;
        }
        return claims;
    }

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            username = getClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expirationDate = null;
        try {
            expirationDate = getClaimsFromToken(token).getExpiration();
        } catch (Exception e) {
        }
        return expirationDate;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate != null && expirationDate.before(new Date());
    }

    public String getToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        userDetails.getAuthorities().forEach(authority -> claims.put("role", authority.getAuthority()));
        return generateToken(claims, userDetails.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + (expiration*1000));
    }

}
