package com.example.twitterclone.service

import com.example.twitterclone.converter.UserConverter
import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.dto.UserDto
import com.example.twitterclone.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserService {

    private final KeycloakService keycloakService;
    private final UserConverter userConverter;
    private final UserRepository userRepository;

    UserService(KeycloakService keycloakService, UserConverter userConverter, UserRepository userRepository) {
        this.keycloakService = keycloakService
        this.userConverter = userConverter
        this.userRepository = userRepository
    }

    UserDto register(UserDto dto){

        UserDto user = keycloakService.registerUser(dto)
        if (user == null || user.keycloakId == null){
            throw new RuntimeException("Error happened during user registration!")
        }
        UserDocument userDocument = userConverter.toDocument(user)
        UserDocument savedUser = userRepository.insert(userDocument)
        return userConverter.documentToDto(savedUser)
    }
}
