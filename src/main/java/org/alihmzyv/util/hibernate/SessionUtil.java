package org.alihmzyv.util.hibernate;

import org.alihmzyv.chapter1.hibernate.Message;
import org.alihmzyv.chapter3.entity.Person;
import org.alihmzyv.chapter3.entity.Ranking;
import org.alihmzyv.chapter3.entity.Skill;
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
                .addAnnotatedClasses(Person.class, Skill.class, Ranking.class, Message.class)
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