package org.alihmzyv.util.hibernate;

import org.alihmzyv.chapter1.hibernate.Message;
import org.alihmzyv.chapter3.entity.Person;
import org.alihmzyv.chapter3.entity.Ranking;
import org.alihmzyv.chapter3.entity.Skill;
import org.alihmzyv.chapter4.entity.Email;
import org.alihmzyv.chapter4.entity.SimpleObject;
import org.alihmzyv.chapter4.entity.orphan.Book;
import org.alihmzyv.chapter4.entity.orphan.Library;
import org.alihmzyv.chapter8.deadlock.entity.Publisher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class SessionUtil {
    private static final SessionUtil instance = new SessionUtil();

    private SessionFactory sessionFactory;

    private SessionUtil() {
        initialize();
    }

    private void initialize() {
        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure()
                        .build();
        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClasses(Person.class, Skill.class, Ranking.class, Message.class, SimpleObject.class, Email.class,
                        Book.class, Library.class, Publisher.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    public static Session getSession() {
        return getInstance().sessionFactory.openSession();
    }

    public static void forceReload() {
        getInstance().initialize();
    }

    private static SessionUtil getInstance() {
        return instance;
    }
}