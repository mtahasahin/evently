package com.github.mtahasahin.evently.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mtahasahin.evently.util.JwtTokenProvider;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String prefix = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        ObjectMapper objectMapper = new ObjectMapper();
        if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {

            try {
                String jwtToken = authorizationHeader.substring(prefix.length());
                var claims = jwtTokenProvider.getClaimsFromJWT(jwtToken);
                ArrayList<String> authorities = (ArrayList<String>) claims.get("authorities");
                var grantedAuthorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));
                User principal = new User(claims.getSubject(), "", grantedAuthorityList);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    var token = new UsernamePasswordAuthenticationToken(principal, null, grantedAuthorityList);
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }

            } catch (Exception ex) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json;charset=utf-8");
                var res = ApiResponse.Error(null,ex.getMessage());
                response.getWriter().write(objectMapper.writeValueAsString(res));
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}