package com.example.twitterclone.service

import com.example.twitterclone.converter.TweetConverter
import com.example.twitterclone.document.TweetDocument
import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.dto.TweetResponseDto
import com.example.twitterclone.repository.TweetRepository
import com.example.twitterclone.repository.UserRepository
import com.example.twitterclone.util.KeycloakUtil
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository
    private final TweetConverter tweetConverter;

    TweetService(TweetRepository tweetRepository, UserRepository userRepository, TweetConverter tweetConverter) {
        this.tweetRepository = tweetRepository
        this.userRepository = userRepository
        this.tweetConverter = tweetConverter
    }

    TweetResponseDto createTweet(String text) {
        String keycloakId = KeycloakUtil.getCurrentKeycloakIdOrThrow()
        UserDocument creator = userRepository.findById(keycloakId).orElseThrow()

        TweetDocument tweetDocument = new TweetDocument()
        tweetDocument.setText(text)
        tweetDocument.setTimeCreated(LocalDateTime.now())
        tweetDocument.setCreator(creator)
        tweetDocument.setLikeList(Collections.emptyList())

        TweetDocument savedTweet = tweetRepository.save(tweetDocument)

        return tweetConverter.toResponseDto(savedTweet)
    }

    TweetResponseDto editTweet(String text, String tweetId) {
        String currentKeycloakId = KeycloakUtil.currentKeycloakIdOrThrow
        TweetDocument tweet = tweetRepository.findById(tweetId).orElseThrow()
        if (tweet.creator.keycloakId != currentKeycloakId) {
            throw new RuntimeException("You don't have access to that tweet")
        }
        tweet.setText(text)
        TweetDocument updatedTweet = tweetRepository.save(tweet)
        return tweetConverter.toResponseDto(updatedTweet)
    }

    String deleteTweet(String tweetId) {
        String currentKeycloakId = KeycloakUtil.currentKeycloakIdOrThrow
        TweetDocument tweet = tweetRepository.findById(tweetId).orElseThrow()
        if (tweet.creator.keycloakId != currentKeycloakId) {
            throw new RuntimeException("You don't have access to that tweet")
        }
        tweetRepository.deleteById(tweetId)
        return "Success"
    }

    void likeTweet(String id) {
        TweetDocument tweet = tweetRepository.findById(id).orElseThrow()
        UserDocument currentUser = userRepository.findById(KeycloakUtil.currentKeycloakIdOrThrow).orElseThrow()
        List<String> likeList = tweet.likeList
                .stream()
                .map { user -> user.keycloakId }
                .collect(Collectors.toList())
        if (!likeList.contains(currentUser.keycloakId)) {
            tweet.likeList.add(currentUser)
        } else {
            tweet.likeList.removeIf { user -> user.keycloakId == currentUser.keycloakId }
        }
        tweetRepository.save(tweet)
    }
}
