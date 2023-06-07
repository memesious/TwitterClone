package com.example.twitterclone.controller

import com.example.twitterclone.dto.CommentResponseDto
import com.example.twitterclone.service.CommentService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
class CommentController {

    private final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PostMapping("/{tweetId}")
    CommentResponseDto comment(@PathVariable String tweetId, @RequestBody String text){
        return commentService.comment(tweetId, text)
    }

    @GetMapping("/{tweetId}")
    List<CommentResponseDto> getCommentsList(@PathVariable String tweetId){
        return commentService.getComments(tweetId)
    }
}
