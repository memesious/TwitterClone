package com.example.twitterclone.converter

import com.example.twitterclone.document.CommentDocument
import com.example.twitterclone.dto.CommentResponseDto
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
class CommentConverter {

    List<CommentResponseDto> toDtoList(Collection<CommentDocument> comments){
        if(comments == null){
            return null
        }
        return comments.stream()
        .map {comment->toDto(comment)}
        .collect(Collectors.toList())
    }

    CommentResponseDto toDto(CommentDocument document){
        if (document==null){
            return null
        }
        CommentResponseDto dto = new CommentResponseDto()
        dto.setCreatorUsername(document.commentCreator.username)
        dto.setText(document.text)
        dto.setTimeCreated(document.timeCreated)
        return dto
    }
}
