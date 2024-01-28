package org.alihmzyv.chapter3.repository;

import org.alihmzyv.chapter3.entity.Person;

public interface PersonRepository {
    Person save(String name);
}
