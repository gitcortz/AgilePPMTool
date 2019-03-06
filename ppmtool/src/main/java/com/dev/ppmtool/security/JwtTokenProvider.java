package com.dev.ppmtool.security;

import com.dev.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dev.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.dev.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
    //generate token
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuedAt(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    //validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch(SignatureException ex) {
            System.out.println("invalid jwt signature");
        }catch (MalformedJwtException ex) {
            System.out.println("invalid jwt Token");
        }catch (ExpiredJwtException ex) {
            System.out.println("expired jwt token");
        }catch (UnsupportedJwtException ex) {
            System.out.println("unsupported jwt token");
        }catch (IllegalArgumentException ex) {
            System.out.println("jwt claims string is empty");
        }
        return false;
    }

    //get user id from token
    public Long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return  Long.parseLong(id);
    }
}
