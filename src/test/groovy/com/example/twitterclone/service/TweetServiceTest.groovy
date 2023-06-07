package com.example.twitterclone.service

import com.example.twitterclone.converter.TweetConverter
import com.example.twitterclone.converter.UserConverter
import com.example.twitterclone.document.TweetDocument
import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.repository.TweetRepository
import com.example.twitterclone.repository.UserRepository
import com.example.twitterclone.util.KeycloakUtil
import org.mockito.MockedStatic
import org.mockito.Mockito
import spock.lang.Specification

class TweetServiceTest extends Specification {

    TweetRepository tweetRepositoryMock
    UserRepository userRepositoryMock
    TweetConverter tweetConverter

    TweetService tweetService

    MockedStatic<KeycloakUtil> keycloakUtilMockedStatic

    def setup() {
        tweetRepositoryMock = Mockito.mock(TweetRepository.class)
        userRepositoryMock = Mockito.mock(UserRepository.class)
        tweetConverter = new TweetConverter(new UserConverter())
        tweetService = new TweetService(
                tweetRepositoryMock,
                userRepositoryMock,
                tweetConverter
        )
        keycloakUtilMockedStatic = Mockito.mockStatic(KeycloakUtil)
    }

    void cleanup() {
        keycloakUtilMockedStatic.close()
    }

    def "getFeed should return the converted DTOs for the feed"() {
        given:
        def currentKeycloakId = "user123"
        def tweet1 = new TweetDocument()
        tweet1.id = "tweet1"
        def tweet2 = new TweetDocument()
        tweet2.id = "tweet2"

        UserDocument subscription1 = new UserDocument()
        subscription1.keycloakId = "user1"
        UserDocument subscription2 = new UserDocument()
        subscription1.keycloakId = "user2"
        UserDocument creator = new UserDocument()
        tweet1.creator = creator
        tweet2.creator = creator
        def subscriptions = [subscription1.keycloakId, subscription2.keycloakId, currentKeycloakId]


        def tweetDocumentList = [tweet1, tweet2]

        Mockito.when(KeycloakUtil.getCurrentKeycloakIdOrThrow()).thenReturn(currentKeycloakId)
        Mockito.when(userRepositoryMock.findById(currentKeycloakId)).thenReturn(Optional.of(new UserDocument(subscriptions: List.of(
                subscription1, subscription2))))
        Mockito.when(tweetRepositoryMock.findAllByCreatorIdIn(subscriptions)).thenReturn(tweetDocumentList)

        when:
        def result = tweetService.getFeed()

        then:
        Mockito.mockingDetails(userRepositoryMock).invocations.size() == 1
        Mockito.mockingDetails(tweetRepositoryMock).invocations.size() == 1

        result instanceof List
        result.size() == 2
    }

    def "getFeed should return the converted DTOs for the feed"() {
        given:
        def currentKeycloakId = "user123"
        def tweet1 = new TweetDocument()
        tweet1.id = "tweet1"
        def tweet2 = new TweetDocument()
        tweet2.id = "tweet2"

        UserDocument subscription1 = new UserDocument()
        subscription1.keycloakId = "user1"
        UserDocument subscription2 = new UserDocument()
        subscription2.keycloakId = "user2"
        UserDocument creator = new UserDocument()
        tweet1.creator = creator
        tweet2.creator = creator
        def subscriptions = [subscription1.keycloakId, subscription2.keycloakId, currentKeycloakId]

        def tweetDocumentList = [tweet1, tweet2]

        Mockito.when(KeycloakUtil.getCurrentKeycloakIdOrThrow()).thenReturn(currentKeycloakId)
        Mockito.when(userRepositoryMock.findById(currentKeycloakId)).thenReturn(Optional.of(new UserDocument(subscriptions: [subscription1, subscription2])))
        Mockito.when(tweetRepositoryMock.findAllByCreatorIdIn(subscriptions)).thenReturn(tweetDocumentList)

        when:
        def result = tweetService.getFeed()

        then:
        Mockito.mockingDetails(userRepositoryMock).invocations.size() == 1
        Mockito.mockingDetails(tweetRepositoryMock).invocations.size() == 1

        result instanceof List
        result.size() == 2
    }

}
