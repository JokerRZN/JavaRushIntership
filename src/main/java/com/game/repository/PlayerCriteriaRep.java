package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PlayerCriteriaRep {
    private final EntityManager em;
    private final CriteriaBuilder criteriaBuilder;

    public PlayerCriteriaRep(EntityManager em) {
        this.em = em;
        this.criteriaBuilder = em.getCriteriaBuilder();
    }

    public List<Player> findPlayers(String name, String title, Race race, Profession profession,
                                    Long fromDate, Long toDate,
                                    Boolean banned, Integer minExperience, Integer maxExperience,
                                    Integer minLevel, Integer maxLevel, Integer pageNumber, Integer pageSize,
                                    PlayerOrder order) {
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        Predicate predicate;
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(criteriaBuilder.like(playerRoot.get("name"), "%" + name + "%"));
        }
        if (title != null) {
            predicates.add(criteriaBuilder.like(playerRoot.get("title"), "%" + title + "%"));
        }
        if (race != null) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("race"), race));
        }
        if (profession != null) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("profession"), profession));
        }
        Date from = new Date();
        Date to = new Date();
        if (fromDate != null) {
            from.setTime(fromDate);
            if (toDate != null) {
                to.setTime(toDate);
                predicates.add(criteriaBuilder.between(playerRoot.get("birthday"), from, to));
            }
            else {
                predicates.add(criteriaBuilder.greaterThan(playerRoot.get("birthday"), from));
            }
        }
        else if (toDate != null) {
            to.setTime(toDate);
            predicates.add(criteriaBuilder.lessThan(playerRoot.get("birthday"), to));
        }
        if (banned != null) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("banned"), banned));
        }
        if (minExperience != null) {
            if (maxExperience != null) {
                predicates.add(criteriaBuilder.between(playerRoot.get("experience"), minExperience, maxExperience));
            }
            else {
                predicates.add(criteriaBuilder.greaterThan(playerRoot.get("experience"), minExperience));
            }
        }
        else if (maxExperience != null) {
            predicates.add(criteriaBuilder.lessThan(playerRoot.get("experience"), maxExperience));
        }
        if (minLevel != null) {
            if (maxLevel != null) {
                predicates.add(criteriaBuilder.between(playerRoot.get("level"), minLevel, maxLevel));
            }
            else {
                predicates.add(criteriaBuilder.greaterThan(playerRoot.get("level"), minLevel));
            }
        }
        else if (maxLevel != null) {
            predicates.add(criteriaBuilder.lessThan(playerRoot.get("level"), maxLevel));
        }
        predicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(criteriaBuilder.asc(playerRoot.get(order.getFieldName())));
        TypedQuery<Player> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    private Long getPlayerCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Player> countRoot = countQuery.from(Player.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return em.createQuery(countQuery).getSingleResult();
    }

    public Long findPlayerCount(String name, String title, Race race, Profession profession,
                                    Long fromDate, Long toDate,
                                    Boolean banned, Integer minExperience, Integer maxExperience,
                                    Integer minLevel, Integer maxLevel) {
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        Predicate predicate;
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(criteriaBuilder.like(playerRoot.get("name"), "%" + name + "%"));
        }
        if (title != null) {
            predicates.add(criteriaBuilder.like(playerRoot.get("title"), "%" + title + "%"));
        }
        if (race != null) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("race"), race));
        }
        if (profession != null) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("profession"), profession));
        }
        Date from = new Date();
        Date to = new Date();
        if (fromDate != null) {
            from.setTime(fromDate);
            if (toDate != null) {
                to.setTime(toDate);
                predicates.add(criteriaBuilder.between(playerRoot.get("birthday"), from, to));
            }
            else {
                predicates.add(criteriaBuilder.greaterThan(playerRoot.get("birthday"), from));
            }
        }
        else if (toDate != null) {
            to.setTime(toDate);
            predicates.add(criteriaBuilder.lessThan(playerRoot.get("birthday"), to));
        }


        if (banned != null) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("banned"), banned));
        }
        if (minExperience != null) {
            if (maxExperience != null) {
                predicates.add(criteriaBuilder.between(playerRoot.get("experience"), minExperience, maxExperience));
            }
            else {
                predicates.add(criteriaBuilder.greaterThan(playerRoot.get("experience"), minExperience));
            }
        }
        else if (maxExperience != null) {
            predicates.add(criteriaBuilder.lessThan(playerRoot.get("experience"), maxExperience));
        }
        if (minLevel != null) {
            if (maxLevel != null) {
                predicates.add(criteriaBuilder.between(playerRoot.get("level"), minLevel, maxLevel));
            }
            else {
                predicates.add(criteriaBuilder.greaterThan(playerRoot.get("level"), minLevel));
            }
        }
        else if (maxLevel != null) {
            predicates.add(criteriaBuilder.lessThan(playerRoot.get("level"), maxLevel));
        }
        predicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        criteriaQuery.where(predicate);
        TypedQuery<Player> typedQuery = em.createQuery(criteriaQuery);
        Long playerCount = getPlayerCount(predicate);
        return playerCount;
    }
}