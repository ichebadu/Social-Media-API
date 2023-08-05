package com.example.social_media_api.repository;

import com.example.social_media_api.entity.User;
import com.example.social_media_api.utils.UserPage;
import com.example.social_media_api.utils.UserSearchCriteria;
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
public class UserCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    public UserCriteriaRepository(EntityManager entityManager){
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<User> findAllWithFilter(UserPage userPage,
                                        UserSearchCriteria userSearchCriteria){

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Predicate predicate = getPredicate(userSearchCriteria, userRoot);
        criteriaQuery.where(predicate);
        setOrder(userPage,criteriaQuery,userRoot);

        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(userPage.getPageNumber() * userPage.getPageNumber());
        typedQuery.setMaxResults(userPage.getPageSize());

        Pageable pageable = getPageable(userPage);

        long userCount = getUserCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(),pageable, userCount);
    }



    private Predicate getPredicate(UserSearchCriteria userSearchCriteria, Root<User> userRoot) {

        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(userSearchCriteria.getUsername())){
            predicates.add(
                    criteriaBuilder.like(userRoot.get("username"),
                            "%" + userSearchCriteria.getUsername() + "%")
            );
        }
        if(Objects.nonNull(userSearchCriteria.getEmail())){
            predicates.add(
                    criteriaBuilder.like(userRoot.get("email"),
                            "%" + userSearchCriteria.getUsername() + "%")
            );
        }

        return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }
    private void setOrder(UserPage userPage,
                          CriteriaQuery<User> criteriaQuery,
                          Root<User> userRoot) {
        if(userPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get(userPage.getSortBy())));
        } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(userRoot.get(userPage.getSortBy())));
        }
    }

    private Pageable getPageable(UserPage userPage) {
        Sort sort = Sort.by(userPage.getSortDirection(), userPage.getSortBy());
        return PageRequest.of(userPage.getPageNumber(),userPage.getPageSize(), sort);
    }

    private long getUserCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
