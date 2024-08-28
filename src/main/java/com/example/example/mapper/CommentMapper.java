package com.example.example.mapper;

import com.example.example.entities.Comment;
import com.example.example.model.*;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(CommentMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper
{
    Comment requestToComment(UpsertCommentRequest request);

    Comment requestToComment(Long id, UpsertCommentRequest request);

    CommentResponse commentToResponse(Comment comment);

    List<CommentResponse> commentListToResponseList(List<Comment> comments);

    default CommentListResponse commentListToCommentListResponse(List<Comment> comments) {
        CommentListResponse response = new CommentListResponse();
        response.setComments(commentListToResponseList(comments));
        return response;
    }
}
