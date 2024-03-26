package com.cos.bank.config.jwt;

import com.cos.bank.config.auth.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// every subsequent request to protected endpoints is intercepted by the JwtAuthorizationFilter.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // check if token exists (verify header)
        if (isHeaderVerify(request, response)) {
            // get token from header
            String token = request.getHeader(JwtValueObject.HEADER).replace(JwtValueObject.TOKEN_PREFIX, "");
            // verify jwt token
            LoginUser loginUser = JwtService.verifyJwtToken(token);

            // create authentication
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

            // update authentication in security context holder
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtValueObject.HEADER);

        if (header == null || !header.startsWith(JwtValueObject.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
