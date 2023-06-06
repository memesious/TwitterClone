package com.example.twitterclone.dto

import lombok.Builder
import lombok.Data
import lombok.Getter
import net.minidev.json.annotate.JsonIgnore

@Data
class UserDto {

    @JsonIgnore
    String keycloakId
    String firstName
    String lastName
    String username
    String email
    Integer age
    String password
}
