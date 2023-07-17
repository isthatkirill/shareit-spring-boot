package ru.practicum.shareit.item.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    Comment toComment(String text, Item item, User author);

    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDtoResponse toCommentDtoResponse(Comment comment);

    @Mapping(target = "authorName", source = "comment.author.name")
    List<CommentDtoResponse> toCommentDtoResponse(List<Comment> comment);

}
