package org.alihmzyv.chapter3.repository.impl;

import jakarta.persistence.Tuple;
import org.alihmzyv.chapter3.entity.Person;
import org.alihmzyv.chapter3.entity.Ranking;
import org.alihmzyv.chapter3.entity.Skill;
import org.alihmzyv.chapter3.repository.PersonRepository;
import org.alihmzyv.chapter3.repository.RankingRepository;
import org.alihmzyv.chapter3.repository.SkillRepository;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Map;
import java.util.stream.Collectors;

public class RankingRepositoryImpl implements RankingRepository {
    private final PersonRepository personRepository = new PersonRepositoryImpl();
    private final SkillRepository skillRepository = new SkillRepositoryImpl();

    @Override
    public void addRanking(String subjectName, String observerName, String skillName, int ranking) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            addRanking(session, subjectName, observerName, skillName, ranking);
            tx.commit();
        }
    }

    private Ranking addRanking(Session session, String subjectName, String observerName, String skillName, int rankingVal) {
        Person subject = personRepository.save(subjectName);
        Person observer = personRepository.save(observerName);
        Skill skill = skillRepository.save(skillName);
        Ranking ranking = new Ranking();
        ranking.setObserver(observer);
        ranking.setSubject(subject);
        ranking.setSkill(skill);
        ranking.setRanking(rankingVal);
        session.persist(ranking);
        return ranking;
    }

    @Override
    public int getRankingFor(String subjectName, String skillName) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Integer ranking = getRankingFor(session, subjectName, skillName);
            tx.commit();
            return ranking;
        }
    }

    private Integer getRankingFor(Session session, String subjectName, String skillName) {
        return session.createQuery("select coalesce(avg(r.ranking), 0) from Ranking r " +
                        "where r.subject.name =: subjectName and " +
                        "r.skill.name =: skillName", Double.class)
                .setParameter("subjectName", subjectName)
                .setParameter("skillName", skillName)
                .uniqueResult()
                .intValue();
    }

    @Override
    public void updateRanking(String subjectName, String observerName, String skillName, int rankingVal) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Ranking ranking = findRanking(session, subjectName, observerName, skillName);
            if (ranking == null) {
                addRanking(session, subjectName, observerName, skillName, rankingVal);
            } else {
                ranking.setRanking(rankingVal);
            }
            tx.commit();
        }
    }

    private Ranking findRanking(Session session, String subjectName, String observerName, String skillName) {
        return session.createQuery("from Ranking r " +
                        "where r.subject.name =: subjectName and " +
                        "r.observer.name =: observerName and " +
                        "r.skill.name =: skillName", Ranking.class)
                .setParameter("subjectName", subjectName)
                .setParameter("observerName", observerName)
                .setParameter("skillName", skillName)
                .uniqueResult();
    }

    @Override
    public void removeRanking(String subjectName, String observerName, String skillName) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            removeRanking(session, subjectName, observerName, skillName);
            tx.commit();
        }
    }

    private void removeRanking(Session session, String subjectName, String observerName, String skillName) {
        Ranking ranking = findRanking(session, subjectName, observerName, skillName);
        if (ranking != null) {
            session.remove(ranking);
        }
    }

    @Override
    public Map<String, Integer> findRankingsFor(String subjectName) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Map<String, Integer> rankingBySkillName = findRankingsFor(session, subjectName);
            tx.commit();
            return rankingBySkillName;
        }
    }

    private Map<String, Integer> findRankingsFor(Session session, String subjectName) {
        return session.createQuery("select r.skill.name as skill_name, cast(avg(r.ranking) as int) as rank from Ranking r " +
                        "where r.subject.name =: subjectName " +
                        "group by r.skill.name", Tuple.class)
                .setParameter("subjectName", subjectName)
                .getResultStream()
                .collect(
                        Collectors.toMap(
                                tuple -> tuple.get("skill_name", String.class),
                                tuple -> tuple.get("rank", Integer.class)
                        ));
    }

    @Override
    public Person findBestPersonFor(String skillName) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Person person = findBestPersonFor(session, skillName);
            tx.commit();
            return person;
        }
    }

    private Person findBestPersonFor(Session session, String skillName) {
        return session.createQuery("select r.subject from Ranking r " +
                        "where r.skill.name =: skillName " +
                        "group by r.subject " +
                        "order by avg(r.ranking) desc", Person.class)
                .setParameter("skillName", skillName)
                .setMaxResults(1)
                .uniqueResult();
    }
}
