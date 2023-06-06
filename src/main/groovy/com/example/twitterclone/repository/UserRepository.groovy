package com.example.twitterclone.repository

import com.example.twitterclone.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<UserDocument, String>{
    Optional<UserDocument> findByUsername(String username)
}
