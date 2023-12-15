package com.denisvasilenko.BlogApp.utills;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtill {
    @Value("${jwtTokenConfiguration.secretKey}")
    private String secret;
    @DurationUnit(ChronoUnit.HOURS)
    @Value("${jwtTokenConfiguration.jwtLifeTime}")
    private Duration jwtLifeTime;
    private SecretKey key;

    public String generateToken(UserDetails userDetails){
        key= Keys.hmacShaKeyFor(secret.getBytes());
        Map<String,Object> claims=new HashMap<>();
        List<String> roleList=userDetails.getAuthorities().stream().map(
                GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles",roleList);

        Date issuedDate=new Date();
        Date expiredDate =new Date(issuedDate.getTime()+jwtLifeTime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(key)
                .compact();
    }

    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
    public List<String> getRoles(String token){
        return getAllClaimsFromToken(token).get("roles",List.class);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
