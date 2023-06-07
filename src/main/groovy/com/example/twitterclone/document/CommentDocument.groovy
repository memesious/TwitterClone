package com.example.twitterclone.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

@Document
class CommentDocument {

    @Id
    String id

    String text

    @DBRef
    UserDocument commentCreator

    @DBRef
    TweetDocument tweet

    String tweetId

    LocalDateTime timeCreated

    void setTweet(TweetDocument tweetDocument) {
        this.tweet = tweetDocument
        if (tweetDocument != null) {
            this.tweetId = tweetDocument.id
        }
    }

}
