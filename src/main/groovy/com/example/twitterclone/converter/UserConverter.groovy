package com.example.twitterclone.converter

import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.dto.UserDto
import com.example.twitterclone.dto.UserResponseDto
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Component

@Component
class UserConverter {

    UserRepresentation dtoToRepresentation(UserDto userDto) {
        if (userDto == null) {
            null
        }
        UserRepresentation representation = new UserRepresentation();
        representation.setUsername(userDto.username);
        representation.setFirstName(userDto.firstName);
        representation.setLastName(userDto.lastName);
        representation.setEmail(userDto.email);
        representation.setEnabled(true);
        representation
    }

    UserDocument toDocument(UserDto userDto) {
        if (userDto == null || userDto.keycloakId == null) {
            return null
        }

        UserDocument userDocument = new UserDocument()
        userDocument.setKeycloakId(userDto.keycloakId)
        userDocument.setFirstName(userDto.firstName)
        userDocument.setLastName(userDto.lastName)
        userDocument.setAge(userDto.age)
        userDocument.setUsername(userDto.username)
        userDocument.setEmail(userDto.email)
        userDocument.setSubscriptions(Collections.emptyList())
        userDocument
    }

    UserDto documentToDto(UserDocument userDocument){
        UserDto dto = new UserDto()
        dto.setEmail(userDocument.email)
        dto.setFirstName(userDocument.firstName)
        dto.setLastName(userDocument.lastName)
        dto.setAge(userDocument.age)
        dto.setUsername(userDocument.username)
        dto
    }

    UserDocument updateDocument(UserDocument existingUser, UserDto userDto){
        if (existingUser == null || userDto == null){
            null
        }
        existingUser.setFirstName(userDto.firstName)
        existingUser.setLastName(userDto.lastName)
        existingUser.setEmail(userDto.email)
        existingUser.setAge(userDto.age)
        existingUser
    }

    UserResponseDto toResponseDto(UserDocument userDocument){
        UserResponseDto responseDto = new UserResponseDto()
        responseDto.setEmail(userDocument.email)
        responseDto.setFirstName(userDocument.firstName)
        responseDto.setLastName(userDocument.lastName)
        responseDto.setAge(userDocument.age)
        responseDto.setUsername(userDocument.username)
        responseDto
    }
}
