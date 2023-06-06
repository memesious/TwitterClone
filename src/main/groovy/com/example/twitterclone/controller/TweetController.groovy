package com.example.twitterclone.controller

import com.example.twitterclone.dto.TweetResponseDto
import com.example.twitterclone.service.TweetService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/tweet", produces = MediaType.APPLICATION_JSON_VALUE)
class TweetController {

    private final TweetService tweetService;

    TweetController(TweetService tweetService) {
        this.tweetService = tweetService
    }

    @GetMapping()
    List<TweetResponseDto> feed(){
        //todo
    }

    @PostMapping()
    TweetResponseDto postTweet(@RequestBody String tweet) {
        return tweetService.createTweet(tweet)
    }

    @PutMapping("/{id}")
    TweetResponseDto editTweet(@PathVariable String id, @RequestBody String tweet) {
        return tweetService.editTweet(tweet, id)
    }

    @DeleteMapping("/{id}")
    void deleteTweet(@PathVariable String id) {
        tweetService.deleteTweet(id)
    }

    @PostMapping("/like/{id}")
    void likeTweet(@PathVariable String id){
        tweetService.likeTweet(id)
    }
}
