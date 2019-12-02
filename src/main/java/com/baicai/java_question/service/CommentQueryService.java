package com.baicai.java_question.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.baicai.java_question.domain.Comment;
import com.baicai.java_question.domain.*; // for static metamodels
import com.baicai.java_question.repository.CommentRepository;
import com.baicai.java_question.service.dto.CommentCriteria;
import com.baicai.java_question.service.dto.CommentDTO;
import com.baicai.java_question.service.mapper.CommentMapper;

/**
 * Service for executing complex queries for {@link Comment} entities in the database.
 * The main input is a {@link CommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentDTO} or a {@link Page} of {@link CommentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentQueryService extends QueryService<Comment> {

    private final Logger log = LoggerFactory.getLogger(CommentQueryService.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentQueryService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * Return a {@link List} of {@link CommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findByCriteria(CommentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comment> specification = createSpecification(criteria);
        return commentMapper.toDto(commentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> findByCriteria(CommentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comment> specification = createSpecification(criteria);
        return commentRepository.findAll(specification, page)
            .map(commentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comment> specification = createSpecification(criteria);
        return commentRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comment> createSpecification(CommentCriteria criteria) {
        Specification<Comment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Comment_.id));
            }
            if (criteria.getCreateId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateId(), Comment_.createId));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Comment_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Comment_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Comment_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Comment_.lastModifiedDate));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(Comment_.question, JoinType.LEFT).get(Question_.id)));
            }
        }
        return specification;
    }
}
