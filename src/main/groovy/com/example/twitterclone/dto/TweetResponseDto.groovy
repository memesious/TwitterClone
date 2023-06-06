package com.example.twitterclone.dto

import java.time.LocalDateTime

class TweetResponseDto {

    String tweetId
    String text
    LocalDateTime timeCreated
    UserResponseDto creator
    Long likeAmount
}
