package com.github.mtahasahin.evently;

import com.github.mtahasahin.evently.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.security.Principal;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        var user = userRepository.findByUsername(customUser.username()).orElseThrow();
        Principal principal = () -> user.getId().toString();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, user.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
