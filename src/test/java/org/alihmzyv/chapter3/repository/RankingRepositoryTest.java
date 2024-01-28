package org.alihmzyv.chapter3.repository;

import org.alihmzyv.chapter3.entity.Person;
import org.alihmzyv.chapter3.repository.impl.RankingRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RankingRepositoryTest {
    RankingRepository service = new RankingRepositoryImpl();
    static final String SCOTT = "Scotball Most";
    static final String GENE = "Gene Showrama";
    static final String CEYLON = "Ceylon";

    @Test
    public void addRanking() {
        service.addRanking("J. C. Smell", "Drew Lombardo", "Mule", 8);
        assertEquals(service.getRankingFor("J. C. Smell", "Mule"), 8);
    }


    @Test
    public void updateExistingRanking() {
        service.addRanking(GENE, SCOTT, CEYLON, 6);
        assertEquals(service.getRankingFor(GENE, CEYLON), 6);
        service.updateRanking(GENE, SCOTT, CEYLON, 7);
        assertEquals(service.getRankingFor(GENE, CEYLON), 7);
    }

    @Test
    public void updateNonexistentRanking() {
        assertEquals(service.getRankingFor(SCOTT, CEYLON), 0);
        service.updateRanking(SCOTT, GENE, CEYLON, 7);
        assertEquals(service.getRankingFor(SCOTT, CEYLON), 7);
    }

    @Test
    public void removeRanking() {
        service.addRanking("R1", "R2", "RS1", 8);
        assertEquals(service.getRankingFor("R1", "RS1"), 8);
        service.removeRanking("R1", "R2", "RS1");
        assertEquals(service.getRankingFor("R1", "RS1"), 0);
    }

    @Test
    public void removeNonexistentRanking() {
        service.removeRanking("R3", "R4", "RS2");
    }

    @Test
    public void validateRankingAverage() {
        service.addRanking("A", "B", "C", 4);
        service.addRanking("A", "B", "C", 5);
        service.addRanking("A", "B", "C", 6);
        assertEquals(service.getRankingFor("A", "C"), 5);
        service.addRanking("A", "B", "C", 7);
        service.addRanking("A", "B", "C", 8);
        assertEquals(service.getRankingFor("A", "C"), 6);
    }

    @Test
    public void findAllRankingsEmptySet() {
        assertEquals(service.getRankingFor("Nobody", "Java"), 0);

        assertEquals(service.getRankingFor("Nobody", "Python"), 0);
        Map<String, Integer> rankings = service.findRankingsFor("Nobody");
// make sure our dataset size is what we expect: empty
        assertEquals(rankings.size(), 0);
    }

    @Test
    public void findAllRankings() {
        assertEquals(service.getRankingFor("Somebody", "Java"), 0);
        assertEquals(service.getRankingFor("Somebody", "Python"), 0);
        service.addRanking("Somebody", "Nobody", "Java", 9);
        service.addRanking("Somebody", "Nobody", "Java", 7);
        service.addRanking("Somebody", "Nobody", "Python", 7);
        service.addRanking("Somebody", "Nobody", "Python", 5);
        Map<String, Integer> rankings = service.findRankingsFor("Somebody");
        assertEquals(rankings.size(), 2);
        assertNotNull(rankings.get("Java"));
        assertEquals(rankings.get("Java"), 8);
        assertNotNull(rankings.get("Python"));
        assertEquals(rankings.get("Python"), 6);
    }

    @Test
    public void findBestForNonexistentSkill() {
        Person p = service.findBestPersonFor("no skill");
        assertNull(p);
    }

    @Test
    public void findBestForSkill() {
        service.addRanking("S1", "O1", "Sk1", 6);
        service.addRanking("S1", "O2", "Sk1", 8);
        service.addRanking("S2", "O1", "Sk1", 5);
        service.addRanking("S2", "O2", "Sk1", 7);
        service.addRanking("S3", "O1", "Sk1", 7);
        service.addRanking("S3", "O2", "Sk1", 9);
// data that should not factor in!
        service.addRanking("S1", "O2", "Sk2", 2);
        Person p = service.findBestPersonFor("Sk1");
        assertEquals(p.getName(), "S3");
    }
}