package com.example.twitterclone.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class UserDocument {

    @Id
    String keycloakId

    String firstName

    String lastName

    String username

    String email

    Integer age

    @DBRef
    List<UserDocument> subscriptions

    @Override
    boolean equals(Object obj) {
        if (!obj instanceof UserDocument){
            false
        }
        if (((UserDocument)obj).keycloakId == this.keycloakId){
            true
        }
        false
    }

    int hashCode() {
        int result
        result = (keycloakId != null ? keycloakId.hashCode() : 0)
        result = 31 * result + (username != null ? username.hashCode() : 0)
        return result
    }
}
