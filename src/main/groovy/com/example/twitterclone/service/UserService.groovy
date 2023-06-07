package com.example.twitterclone.service

import com.example.twitterclone.converter.UserConverter
import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.dto.UserDto
import com.example.twitterclone.repository.UserRepository
import com.example.twitterclone.util.KeycloakUtil
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

    UserDto register(UserDto dto) {

        UserDto user = keycloakService.registerUser(dto)
        if (user == null || user.keycloakId == null) {
            throw new RuntimeException("Error happened during user registration!")
        }
        UserDocument userDocument = userConverter.toDocument(user)
        UserDocument savedUser = userRepository.insert(userDocument)
        return userConverter.documentToDto(savedUser)
    }

    UserDto updateAccount(UserDto dto) {
        String keycloakId = KeycloakUtil.getCurrentKeycloakIdOrThrow()
        UserDocument user = userRepository.findById(keycloakId)
        //if data not found but keycloak id is exist, sawing new data
                .orElse(new UserDocument())

        UserDocument updatedUser = userConverter.updateDocument(user, dto)

        return userConverter.documentToDto(userRepository.save(updatedUser))
    }

    void subscribe(String username) {
        String keycloakId = KeycloakUtil.currentKeycloakIdOrThrow
        UserDocument subscriptionUser = userRepository.findByUsername(username).orElseThrow()
        UserDocument currentUser = userRepository.findById(keycloakId).orElseThrow()

        boolean isInSubscriptions = currentUser.subscriptions.stream()
                .map { subscriber -> subscriber.username }
                .anyMatch { subscriberUsername -> subscriberUsername == username }

        if (!isInSubscriptions) {
            currentUser.subscriptions.add(subscriptionUser)
            userRepository.save(currentUser)
        }
    }

    void unSubscribe(String username) {
        String keycloakId = KeycloakUtil.currentKeycloakIdOrThrow
        UserDocument subscriptionUser = userRepository.findByUsername(username).orElseThrow()
        UserDocument currentUser = userRepository.findById(keycloakId).orElseThrow()

        boolean isInSubscriptions = currentUser.subscriptions.stream()
                .map { subscriber -> subscriber.username }
                .anyMatch { subscriberUsername -> subscriberUsername == username }

        if (isInSubscriptions) {
            currentUser.subscriptions.removeIf { subscriber -> subscriber.keycloakId == subscriptionUser.keycloakId }
            userRepository.save(currentUser)
        }
    }

    void deleteAccount(){
        String currentKeycloakId = KeycloakUtil.getCurrentKeycloakIdOrThrow()
        keycloakService.deleteUser(currentKeycloakId)
        userRepository.deleteById(currentKeycloakId)
    }
}
