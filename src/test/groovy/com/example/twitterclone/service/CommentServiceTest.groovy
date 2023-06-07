package com.example.twitterclone.service

import com.example.twitterclone.converter.CommentConverter
import com.example.twitterclone.document.CommentDocument
import com.example.twitterclone.document.TweetDocument
import com.example.twitterclone.document.UserDocument
import com.example.twitterclone.dto.CommentResponseDto
import com.example.twitterclone.repository.CommentRepository
import com.example.twitterclone.repository.TweetRepository
import com.example.twitterclone.repository.UserRepository
import com.example.twitterclone.util.KeycloakUtil
import org.assertj.core.api.Assertions
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.verification.VerificationMode

import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.Mockito.times

class CommentServiceTest extends Specification {


    TweetRepository tweetRepositoryMock
    UserRepository userRepositoryMock
    CommentRepository commentRepositoryMock
    CommentConverter commentConverter

    CommentService commentService


    def setup() {
        userRepositoryMock = Mockito.mock(UserRepository.class)
        tweetRepositoryMock = Mockito.mock(TweetRepository.class)
        commentRepositoryMock = Mockito.mock(CommentRepository.class)
        commentConverter = new CommentConverter()
        commentService = new CommentService(
                tweetRepositoryMock,
                userRepositoryMock,
                commentRepositoryMock,
                commentConverter
        )
    }

    @Unroll
    def "comment should save a new comment and return the converted DTO"() {
        given:
        def tweetId = "tweet123"
        def text = "This is a comment."
        def currentUser = new UserDocument()
        currentUser.keycloakId = "user123"
        def tweetDocument = new TweetDocument()
        tweetDocument.id = tweetId

        CommentResponseDto expectedResult = new CommentResponseDto()
        expectedResult.setText(text)
        expectedResult.setCreatorUsername(currentUser.username)


        Mockito.mockStatic(KeycloakUtil)
        Mockito.when(KeycloakUtil::getCurrentKeycloakIdOrThrow()).thenReturn(currentUser.keycloakId)

        Mockito.when(userRepositoryMock.findById(currentUser.keycloakId))
                .thenReturn(Optional.of(currentUser))

        Mockito.when(tweetRepositoryMock.findById(tweetId))
                .thenReturn(Optional.of(tweetDocument))

        Mockito.when(commentRepositoryMock.save(ArgumentMatchers.any()))
                .then(inv -> inv.getArgument(0))

        when:
        def result = commentService.comment(tweetId, text)

        then:
        Mockito.mockingDetails(userRepositoryMock).invocations.size() == 1
        Mockito.mockingDetails(tweetRepositoryMock).invocations.size() == 1
        Mockito.mockingDetails(commentRepositoryMock).invocations.size() == 1


        Assertions.assertThat(result).usingRecursiveComparison()
                .ignoringFields("timeCreated")
                .isEqualTo(expectedResult)
    }

    @Unroll
    def "getComments should return the converted DTOs for the comments"() {
        given:
        def tweetId = "tweet123"
        def creator = new UserDocument()
        creator.username = "username"
        def comment1 = new CommentDocument()
        comment1.id = "comment1"
        comment1.commentCreator = creator
        def comment2 = new CommentDocument()
        comment2.id = "comment2"
        comment2.commentCreator = creator
        def comments = [comment1, comment2]

        Mockito.when(tweetRepositoryMock.existsById(tweetId)).thenReturn(true)
        Mockito.when(commentRepositoryMock.findAllByTweetId(tweetId)).thenReturn(comments)

        when:
        def result = commentService.getComments(tweetId)

        then:
        Mockito.mockingDetails(tweetRepositoryMock).invocations.size() == 1
        Mockito.mockingDetails(commentRepositoryMock).invocations.size() == 1

        result instanceof List
        result.size() == 2


        Assertions.assertThat(result).usingRecursiveComparison()
                .ignoringFields("timeCreated")
                .isEqualTo(commentConverter.toDtoList(comments))
    }
}
