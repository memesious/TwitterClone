package com.example.twitterclone.controller

import com.example.twitterclone.util.KeycloakUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/info")
    public String getInfo(){
        def orThrow = KeycloakUtil.getCurrentKeycloakIdOrThrow()
        return orThrow
    }
}
