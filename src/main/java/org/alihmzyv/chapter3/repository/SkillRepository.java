package org.alihmzyv.chapter3.repository;

import org.alihmzyv.chapter3.entity.Skill;

public interface SkillRepository {
    Skill save(String skillName);
}
