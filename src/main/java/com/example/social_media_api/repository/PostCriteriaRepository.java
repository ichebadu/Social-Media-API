package com.example.social_media_api.repository;

import com.example.social_media_api.entity.Post;
import com.example.social_media_api.utils.PostCriteriaSearch;
import com.example.social_media_api.utils.PostPage;
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
public class PostCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public PostCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Post> findAllWithFilters(PostPage postPage, PostCriteriaSearch postCriteriaSearch) {

        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root<Post> postRoot = criteriaQuery.from(Post.class);
        Predicate predicate = getPredicate(postCriteriaSearch, postRoot);
        criteriaQuery.where(predicate);
        setOrder(postPage, criteriaQuery, postRoot);

        TypedQuery<Post> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(postPage.getPageNumber() * postPage.getPageSize());
        typedQuery.setMaxResults(postPage.getPageSize());

        Pageable pageable = getPageable(postPage);

        long postCount = getPostCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, postCount);
    }

    private Predicate getPredicate(PostCriteriaSearch postCriteriaSearch, Root<Post> postRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(postCriteriaSearch.getContent())) {
            predicates.add(
                    criteriaBuilder.like(postRoot.get("content"),
                            "%" + postCriteriaSearch.getContent() + "%")
            );
        }

        if (Objects.nonNull(postCriteriaSearch.getTitle())) {
            predicates.add(
                    criteriaBuilder.like(postRoot.get("title"),
                            "%" + postCriteriaSearch.getTitle() + "%")
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(PostPage postPage,
                          CriteriaQuery<Post> criteriaQuery,
                          Root<Post> postRoot) {
        if (postPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(postRoot.get(postPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(postRoot.get(postPage.getSortBy())));
        }
    }

    private Pageable getPageable(PostPage postPage) {
        Sort sort = Sort.by(postPage.getSortDirection(), postPage.getSortBy());
        return PageRequest.of(postPage.getPageNumber(), postPage.getPageSize(), sort);
    }

    private long getPostCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Post> countRoot = countQuery.from(Post.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
