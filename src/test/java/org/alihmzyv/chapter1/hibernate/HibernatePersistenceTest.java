package org.alihmzyv.chapter1.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HibernatePersistenceTest {
    private static SessionFactory factory = null;

    @BeforeAll
    public static void setup() {
        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure()
                        .build();
        factory = new MetadataSources(registry)
                .addAnnotatedClass(Message.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    public Message saveMessage(String text) {
        Message message = new Message();
        message.setText(text);
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(message);
            tx.commit();
        }
        return message;
    }

    @Test
    public void readMessage() {
        Message savedMessage = saveMessage("Hello, World");
        List<Message> list;
        try (Session session = factory.openSession()) {
            list = session
                    .createQuery("from Message", Message.class)
                    .list();
        }
        assertEquals(list.size(), 1);
        for (Message m : list) {
            System.out.println(m);
        }
        assertEquals(list.get(0), savedMessage);
    }
}
