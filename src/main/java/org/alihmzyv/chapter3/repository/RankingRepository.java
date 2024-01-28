package org.alihmzyv.chapter3.repository;

import org.alihmzyv.chapter3.entity.Person;

import java.util.Map;

public interface RankingRepository {
    void addRanking(String subjectName, String observerName, String skillName, int ranking);

    int getRankingFor(String subjectName, String skillName);

    void updateRanking(String subjectName, String observerName, String skillName, int ranking);

    void removeRanking(String subjectName, String observerName, String skillName);

    Map<String, Integer> findRankingsFor(String subjectName);

    Person findBestPersonFor(String skillName);
}
