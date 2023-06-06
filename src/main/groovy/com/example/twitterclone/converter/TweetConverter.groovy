package com.example.twitterclone.converter

import com.example.twitterclone.document.TweetDocument
import com.example.twitterclone.dto.TweetResponseDto
import org.springframework.stereotype.Component

@Component
class TweetConverter {

    private final UserConverter userConverter

    TweetConverter(UserConverter userConverter) {
        this.userConverter = userConverter
    }

    TweetResponseDto toResponseDto(TweetDocument tweetDocument) {
        if (tweetDocument == null) {
            return null
        }
        TweetResponseDto dto = new TweetResponseDto()
        dto.setTweetId(tweetDocument.id)
        dto.setTimeCreated(tweetDocument.timeCreated)
        dto.setText(tweetDocument.text)
        dto.setCreator(userConverter.toResponseDto(tweetDocument.creator))

        def likeAmount = tweetDocument.likeList
        dto.setLikeAmount(likeAmount == null ? 0 : likeAmount.size())
        dto
    }
}
