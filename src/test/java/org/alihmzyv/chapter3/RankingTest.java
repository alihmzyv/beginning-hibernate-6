package org.alihmzyv.chapter3;

import org.alihmzyv.chapter3.entity.Person;
import org.alihmzyv.chapter3.entity.Ranking;
import org.alihmzyv.chapter3.entity.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RankingTest {
    static SessionFactory sessionFactory;

    @BeforeAll
    static void setUp() {
        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure()
                        .build();
        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClasses(Person.class, Skill.class, Ranking.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    @BeforeEach
    void populateRankingData() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            createData(session, "J. C. Smell", "Gene Showrama", "Java", 6);
            createData(session, "J. C. Smell", "Scottball Most", "Java", 7);
            createData(session, "J. C. Smell", "Drew Lombardo", "Java", 8);
            tx.commit();
        }
    }

    private void createData(Session session, String subjectName, String observerName, String skillName, int rankingVal) {
        Person subject = savePerson(session, subjectName);
        Person observer = savePerson(session, observerName);
        Skill skill = saveSkill(session, skillName);
        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rankingVal);
        session.persist(ranking);
    }

    private Skill saveSkill(Session session, String skillName) {
        Skill skill = findSkill(session, skillName);
        if (skill == null) {
            skill = new Skill();
            skill.setName(skillName);
            session.persist(skill);
        }
        return skill;
    }

    private Skill findSkill(Session session, String name) {
        return session.createQuery("from Skill s where s.name =: name", Skill.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    private Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);
        if (person == null) {
            person = new Person();
            person.setName(name);
            session.persist(person);
        }
        return person;
    }

    private Person findPerson(Session session, String name) {
        return session.createQuery("from Person2 p where p.name =: name", Person.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Test
    public void testRankings() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            IntSummaryStatistics summaryStats = session.createQuery(
                            "from Ranking r "
                                    + "where r.subject.name=:subjectName "
                                    + "and r.skill.name=:skill", Ranking.class)
                    .setParameter("subjectName", "J. C. Smell")
                    .setParameter("skill", "Java")
                    .stream()
                    .collect(Collectors.summarizingInt(Ranking::getRanking));
            long count = summaryStats.getCount();
            int average = (int) summaryStats.getAverage();
            tx.commit();
            session.close();
            assertEquals(3, count);
            assertEquals(7, average);
        }
    }

    private int getAverage(String subjectName, String skillName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            IntSummaryStatistics summaryStats = session.createQuery(
                            "from Ranking r "
                                    + "where r.subject.name=:subjectName "
                                    + "and r.skill.name=:skill", Ranking.class)
                    .setParameter("subjectName", "J. C. Smell")
                    .setParameter("skill", "Java")
                    .stream()
                    .collect(Collectors.summarizingInt(Ranking::getRanking));
            tx.commit();
            return (int) summaryStats.getAverage();
        }
    }

    @Test
    public void changeRanking() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Ranking ranking = session.createQuery("from Ranking r " +
                            "where r.subject.name =: subjectName and " +
                            "r.observer.name =: observerName and " +
                            "r.skill.name =: skillName", Ranking.class)
                    .setParameter("subjectName", "J. C. Smell")
                    .setParameter("observerName", "Gene Showrama")
                    .setParameter("skillName", "Java")
                    .uniqueResult();
            assertNotNull(ranking, "Ranking should exist");
            ranking.setRanking(9);
            tx.commit();
        }

        assertEquals(8, getAverage("J. C. Smell", "Java"));
    }

    @Test
    public void removeRanking() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Ranking ranking = findRanking(session, "J. C. Smell", "Gene Showrama", "Java");
            session.remove(ranking);
            tx.commit();
        }
        assertEquals(getAverage("J. C. Smell", "Java"), 7);
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
}

