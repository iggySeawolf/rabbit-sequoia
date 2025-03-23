package xyz.iggy.rabbit_arnab.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final byte[] secretKey = Base64.getDecoder().decode("7bWnX9lOJw+M8Q5ZdF1P7G+2XjYqzXh5lFfA0J9F0Qo=");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            String jwt = Jwts.builder()
                    .setSubject("user123")
                    .signWith(Keys.hmacShaKeyFor(secretKey), SignatureAlgorithm.HS256)
//                    .signWith()
                    .compact();

            Key key = Keys.hmacShaKeyFor(secretKey/*.getBytes(StandardCharsets.UTF_8)*/);
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            if (username != null) {
                User user = new User(username, "", Collections.emptyList());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
