package com.example.twitterclone.document

import org.springframework.data.annotation.Id

class UserDocument {

    @Id
    String keycloakId

    String firstName

    String lastName

    String username

    String email

    Integer age
}
