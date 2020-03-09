package com.blog.aigetcode.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface TokenProvider {

    String getUsernameFromToken(String token);
    Date getExpirationDateFromToken(String token);
    String generateToken(Authentication authentication);
    String generateRefreshToken(String email);
    Boolean validateToken(String token, UserDetails userDetails);
    UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final UserDetails userDetails);

}
