package com.example.bankcards.util;

import com.example.bankcards.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    public String generateToken(UserDetailsImpl userDetailsImpl) {

        Map<String, Object> claims = new HashMap<>();

        List<String> roleList = userDetailsImpl.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roleList);

        Date issuedDate = new Date();

        Date exoiredDate = new Date(issuedDate.getTime()  + jwtLifeTime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetailsImpl.getUsername())
                .setExpiration(exoiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getCardHolderName(String jwtToken) {
        return getAllClaimsFromToken(jwtToken).getSubject();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getCardHolderName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
