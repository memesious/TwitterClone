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
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class CommentService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository
    private final CommentRepository commentRepository
    private final CommentConverter commentConverter

    CommentService(TweetRepository tweetRepository, UserRepository userRepository, CommentRepository commentRepository, CommentConverter commentConverter) {
        this.tweetRepository = tweetRepository
        this.userRepository = userRepository
        this.commentRepository = commentRepository
        this.commentConverter = commentConverter
    }

    CommentResponseDto comment(String tweetId, String text) {
        String currentKeycloakId = KeycloakUtil.currentKeycloakIdOrThrow
        UserDocument currentUser = userRepository.findById(currentKeycloakId).orElseThrow()
        TweetDocument tweetDocument = tweetRepository.findById(tweetId).orElseThrow()
        CommentDocument comment = new CommentDocument()
        comment.setTweet(tweetDocument)
        comment.setCommentCreator(currentUser)
        comment.setTimeCreated(LocalDateTime.now())
        comment.setText(text)
        CommentDocument savedComment = commentRepository.save(comment)
        return commentConverter.toDto(savedComment)
    }

    List<CommentResponseDto> getComments(String tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new RuntimeException("Couldn't find the tweet!")
        }
        List<CommentDocument> comments = commentRepository.findAllByTweetId(tweetId)
        return commentConverter.toDtoList(comments)
    }
}
