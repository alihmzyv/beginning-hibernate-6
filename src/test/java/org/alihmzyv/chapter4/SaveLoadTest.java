package org.alihmzyv.chapter4;

import org.alihmzyv.chapter4.entity.SimpleObject;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

public class SaveLoadTest {
    @Test
    public void testSaveLoad() {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();
            SimpleObject obj = new SimpleObject();
            obj.setKey("k1");
            obj.setValue(10L);
            session.persist(obj);
            tx.commit();
        }

        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();
            int obj = session.createQuery("update SimpleObject set value = 1000L where id = 1")
                            .executeUpdate();
            System.out.println("Updated rows: " + obj);
            remove();
            tx.commit();
        }
    }

    private void remove() {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();
            int delete = session.createQuery("delete from SimpleObject where id = 1")
                    .executeUpdate();
            System.out.println("Deleted: " + delete);
            tx.commit();
        }
    }
}
