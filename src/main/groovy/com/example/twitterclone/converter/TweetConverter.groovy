package com.example.twitterclone.converter

import com.example.twitterclone.document.TweetDocument
import com.example.twitterclone.dto.TweetResponseDto
import com.example.twitterclone.util.KeycloakUtil
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
class TweetConverter {

    private final UserConverter userConverter

    TweetConverter(UserConverter userConverter) {
        this.userConverter = userConverter
    }

    List<TweetResponseDto> toResponseDtoList(Collection tweetDocumentList) {
        if (tweetDocumentList == null) {
            return null
        }
        return tweetDocumentList.stream()
                .map { tweet->toResponseDto(tweet) }
                .collect(Collectors.toList())
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
