package com.example.twitterclone.controller

import com.example.twitterclone.dto.UserDto
import com.example.twitterclone.service.KeycloakService
import com.example.twitterclone.service.UserService
import com.example.twitterclone.util.KeycloakUtil
import lombok.RequiredArgsConstructor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService
    }

    @PostMapping("/register")
    String register(@RequestBody UserDto userDto) {
        return userService.register(userDto)
    }

    @PutMapping("/update")
    String update(@RequestBody UserDto userDto) {
        return userService.updateAccount(userDto)
    }

    @PostMapping("/subscribe/{username}")
    void subscribe(@PathVariable String username) {
        userService.subscribe(username)
    }

    @PostMapping("/unsubscribe/{username}")
    void unSubscribe(@PathVariable String username) {
        userService.unSubscribe(username)
    }

}
