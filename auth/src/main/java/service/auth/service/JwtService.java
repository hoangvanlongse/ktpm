package service.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtService implements IJwtService {

    public static final String SECRET = "eyV/h2wsob5ZqKHAm0/0ppH+0+WFQJBN17qyxYmKCVE=";

    public static final byte[] BASE64_ENCODED_SECRECT = Base64.getEncoder().encode(SECRET.getBytes());

    @Override
    public String generateToken(String email, String roles) { // Use email as username
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return createToken(claims, email);
    }

    @Override
    public String generateToken(String email) { // Use email as username
        return generateToken(email, "STUDENT");
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        if (claims == null)
            return null;

        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException
                | IllegalArgumentException e) {
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            // Verify that the token is valid

            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);

            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException
                | IllegalArgumentException e) {
            return false;
        }

    }
}