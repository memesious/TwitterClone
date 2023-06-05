package com.example.twitterclone.util

import org.keycloak.KeycloakPrincipal
import org.keycloak.KeycloakSecurityContext
import org.keycloak.representations.JsonWebToken
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

final class KeycloakUtil {

    public static String getCurrentKeycloakIdOrThrow() {
        return getCurrentKeycloakId()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't get current keycloak ID!", HttpStatus.UNAUTHORIZED));
    }

    public static Optional<String> getCurrentKeycloakId() {
        return getKeycloakSecurityContext()
                .map(KeycloakSecurityContext::getToken)
                .map(JsonWebToken::getSubject);
    }

    private static Optional<KeycloakSecurityContext> getKeycloakSecurityContext() {
        final Object principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .flatMap(c -> Optional.ofNullable(c.getAuthentication()))
                .map(Authentication::getPrincipal)
                .orElse(null);

        if (principal == null) {
            return Optional.empty();
        }

        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal
            return Optional.ofNullable(keycloakPrincipal.keycloakSecurityContext)
        }
        return Optional.empty();
    }
}
