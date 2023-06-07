package com.example.twitterclone.repository

import com.example.twitterclone.document.CommentDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository extends MongoRepository<CommentDocument, String>{

    List<CommentDocument> findAllByTweetId(String tweetId)

}