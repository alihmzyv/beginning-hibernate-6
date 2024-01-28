package org.alihmzyv.chapter3.repository.impl;

import org.alihmzyv.chapter3.entity.Person;
import org.alihmzyv.chapter3.repository.PersonRepository;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PersonRepositoryImpl implements PersonRepository {
    @Override
    public Person save(String name) {
        Session session = SessionUtil.getSession();
        try (session) {
            Transaction tx = session.getTransaction();
            tx.begin();
            Person person = save(session, name);
            tx.commit();
            return person;
        }
    }

    private Person save(Session session, String name) {
        Person person = new Person();
        person.setName(name);
        session.persist(person);
        return person;
    }
}
