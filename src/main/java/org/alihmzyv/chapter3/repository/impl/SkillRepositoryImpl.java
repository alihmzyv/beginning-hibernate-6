package org.alihmzyv.chapter3.repository.impl;

import org.alihmzyv.chapter3.entity.Skill;
import org.alihmzyv.chapter3.repository.SkillRepository;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SkillRepositoryImpl implements SkillRepository {
    @Override
    public Skill save(String skillName) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Skill skill = save(session, skillName);
            tx.commit();
            return skill;
        }
    }

    private Skill save(Session session, String skillName) {
        Skill skill = new Skill();
        skill.setName(skillName);
        session.persist(skill);
        return skill;
    }
}
