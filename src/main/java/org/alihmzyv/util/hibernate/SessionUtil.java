package org.alihmzyv.util.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

@Slf4j
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