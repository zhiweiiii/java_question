package com.baicai.java_question.service.mapper;

import com.baicai.java_question.domain.*;
import com.baicai.java_question.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "question.id", target = "questionId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "questionId", target = "question")
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
