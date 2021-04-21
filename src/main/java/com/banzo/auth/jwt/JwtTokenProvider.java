package com.banzo.auth.jwt;

import com.banzo.auth.exception.BadRequestException;
import com.banzo.auth.model.Role;
import com.banzo.auth.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${auth.jwt.secret-key}")
    private String secretKey;

    @Value("${auth.jwt.expiration-ms}")
    private Long validityInMs;

    private final UserService userService;

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(String subject, Collection<Role> roles) {

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("auth", roles
                .stream()
                .map(s -> new SimpleGrantedAuthority(s.getName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            throw new BadRequestException("Invalid or expired JWT token");
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(getUsername(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
