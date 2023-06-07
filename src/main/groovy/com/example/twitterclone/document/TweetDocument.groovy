package com.example.twitterclone.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

@Document
class TweetDocument {

    @Id
    String id;

    String text

    LocalDateTime timeCreated

    @DBRef
    UserDocument creator

    String creatorId

    @DBRef
    List<UserDocument> likeList

    void setCreator(UserDocument creator){
        this.creator = creator
        this.creatorId = creator.keycloakId
    }
}
