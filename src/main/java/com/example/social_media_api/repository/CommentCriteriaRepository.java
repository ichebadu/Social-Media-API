package com.example.social_media_api.repository;

import com.example.social_media_api.entity.Comment;
import com.example.social_media_api.utils.CommentCriteriaSearch;
import com.example.social_media_api.utils.CommentPage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CommentCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public CommentCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Comment> findAllWithFilter(CommentPage commentPage, CommentCriteriaSearch commentCriteriaSearch) {
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> commentRoot = criteriaQuery.from(Comment.class);
        Predicate predicate = getPredicate(commentCriteriaSearch, commentRoot);
        criteriaQuery.where(predicate);
        setOrder(commentPage, criteriaQuery, commentRoot);

        TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(commentPage.getPageNumber() * commentPage.getPageSize());
        typedQuery.setMaxResults(commentPage.getPageSize());

        Pageable pageable = getPageable(commentPage);

        long commentCount = getCommentCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, commentCount);
    }

    private Predicate getPredicate(CommentCriteriaSearch commentCriteriaSearch, Root<Comment> commentRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(commentCriteriaSearch.getContent())) {
            predicates.add(
                    criteriaBuilder.like(commentRoot.get("content"),
                            "%" + commentCriteriaSearch.getContent() + "%")
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(CommentPage commentPage, CriteriaQuery<Comment> criteriaQuery, Root<Comment> commentRoot) {
        if (commentPage.getSortDirection().isAscending()) {
            criteriaQuery.orderBy(criteriaBuilder.asc(commentRoot.get(commentPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(commentRoot.get(commentPage.getSortBy())));
        }
    }

    private Pageable getPageable(CommentPage commentPage) {
        Sort sort = Sort.by(commentPage.getSortDirection(), commentPage.getSortBy());
        return PageRequest.of(commentPage.getPageNumber(), commentPage.getPageSize(), sort);
    }

    private long getCommentCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Comment> countRoot = countQuery.from(Comment.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
