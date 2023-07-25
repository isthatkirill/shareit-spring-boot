package isthatkirill.shareit.item.comment.mapper;

import isthatkirill.shareit.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import isthatkirill.shareit.item.comment.dto.CommentDtoResponse;
import isthatkirill.shareit.item.comment.model.Comment;
import isthatkirill.shareit.item.model.Item;

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
