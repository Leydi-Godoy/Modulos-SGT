package com.sgturnos.security;

import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/sgturnos/dashboard_admin");
        } else if (
                roles.contains("ROLE_AUX") ||
                roles.contains("ROLE_ENF") ||
                roles.contains("ROLE_MED") ||
                roles.contains("ROLE_TER")
        ) {
            response.sendRedirect("/sgturnos/dashboard_usuario");
        } else {
            response.sendRedirect("/sgturnos/error_rol");
        }
    }
}