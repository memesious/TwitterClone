package com.example.twitterclone.repository

import com.example.twitterclone.document.TweetDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface TweetRepository extends MongoRepository<TweetDocument, String>{

    List<TweetDocument> findAllByCreatorIdIn(List<String> creatorList)
}