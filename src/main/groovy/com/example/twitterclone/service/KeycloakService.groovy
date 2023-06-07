package com.example.twitterclone.service

import com.example.twitterclone.converter.UserConverter
import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.dto.UserDto
import com.example.twitterclone.util.KeycloakUtil
import groovy.util.logging.Slf4j
import lombok.RequiredArgsConstructor
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.RoleScopeResource
import org.keycloak.admin.client.resource.RolesResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service

import javax.ws.rs.core.Response

@Slf4j
@Service
class KeycloakService {
    private final KeycloakSpringBootProperties keycloakProperties;
    private final UsersResource usersResource;
    private final RealmResource realmResource;
    private final UserConverter userConverter;

    KeycloakService(KeycloakSpringBootProperties keycloakProperties, UserConverter userConverter, Keycloak keycloak) {
        this.keycloakProperties = keycloakProperties
        this.userConverter = userConverter
        this.realmResource = keycloak.realm(keycloakProperties.getRealm())
        this.usersResource = realmResource.users()
    }

    UserDto registerUser(UserDto user) {
        final UserRepresentation representation = userConverter.dtoToRepresentation(user);
        Response response = createUser(representation);
        if (response.getStatus() != 201) {
            log.error("Error trying register the user " + user.username);
            return null
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        user.setKeycloakId(userId);
        log.info("User created: [id: {}, username: {}]", userId, user.username);

        UserResource userResource = findUserById(userId);
        setRole(userResource, "USER");

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(user.password);
        cred.setTemporary(false);
        userResource.resetPassword(cred)

        return user;
    }

    public Response createUser(UserRepresentation representation) {
        return usersResource.create(representation);
    }

    UserResource findUserById(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("Empty userId isn't allowed!");
        }

        final UserResource user = usersResource.get(userId);
        if (user != null) {
            return user;
        }
        throw new RuntimeException("No user with provided username found.");
    }

     void setRole(UserResource userResource, String keycloakRole) {
        ClientRepresentation clientRepresentation = realmResource.clients()
                .findByClientId(keycloakProperties.getResource())
                .get(0);
        RolesResource roles = realmResource.clients().get(clientRepresentation.getId()).roles();

        List<RoleRepresentation> keycloakRoles = List.of(roles.get(keycloakRole).toRepresentation());

        final RoleScopeResource roleScopeResource = userResource.roles().clientLevel(clientRepresentation.getId());
        roleScopeResource.remove(roleScopeResource.listAll());
        roleScopeResource.add(keycloakRoles);
    }

    void deleteUser(String keycloakId){
        usersResource.get(keycloakId).remove()
    }
}
