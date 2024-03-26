package com.cos.bank.config.jwt;

import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.user.dto.LoginDto;
import com.cos.bank.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    // when client request POST "/api/login"
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        log.debug("DEBUG: attemptAuthentication is created");

        try {
            ObjectMapper om = new ObjectMapper();
            LoginDto.Request loginDto = om.readValue(request.getInputStream(), LoginDto.Request.class);
            // generate token
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()
            );
            // attempts to authenticate the user using the provided credentials.
            // UserDetailsService.loadUserByUsername -> LoginUser created
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }


    //if attemptAuthentication works, this method will be called
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        log.debug("DEBUG: successfulAuthentication is created");
        // get UserDetails
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        // create JWT
        String jwtToken = JwtService.createJwtToken(loginUser);
        response.addHeader(JwtValueObject.HEADER, jwtToken);
        // create response object
        LoginDto.Response loginDto = LoginDto.Response.of(loginUser.getUser());
        // send response to the client
        CustomResponseUtil.authenticationSuccess(response, "Login succeeded!", loginDto);
    }

    //if attemptAuthentication throw exception, this method will be called
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.noAuthentication(response, "Login failed.");
    }
}
