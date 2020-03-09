package com.blog.aigetcode.controllers;

import com.blog.aigetcode.DTO.AuthToken;
import com.blog.aigetcode.DTO.LoginUserDto;
import com.blog.aigetcode.service.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/token")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider jwtTokenUtil;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/generate")
    public ResponseEntity<AuthToken> register(@RequestBody LoginUserDto loginUser) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(loginUser.getEmail());
        final Date expiredIn = jwtTokenUtil.getExpirationDateFromToken(token);

        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setRefreshToken(refreshToken);
        authToken.setExpiredIn(expiredIn.getTime());

        return ResponseEntity.ok(authToken);
    }

}
