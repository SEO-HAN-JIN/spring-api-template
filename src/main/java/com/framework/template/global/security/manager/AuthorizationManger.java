package com.framework.template.global.security.manager;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AuthorizationManger implements AuthorizationManager<AuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, AuthorizationContext object) {
        Authentication auth = authentication.get();
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        boolean hasPermission = auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMin"));

        return new AuthorizationDecision(hasPermission);
    }
}
