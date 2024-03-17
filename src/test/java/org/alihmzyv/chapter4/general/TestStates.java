package org.alihmzyv.chapter4.general;

import org.alihmzyv.chapter4.entity.SimpleObject;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStates {
    @Test
    public void testMerge() {
        Long id;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.setKey("testMerge");
            simpleObject.setValue(1L);
            session.persist(simpleObject);
            id = simpleObject.getId();
            tx.commit();
        }
        SimpleObject so = ValidateSimpleObject.validate(id, 1L, "testMerge");
        so.setValue(2L);

        try (Session session = SessionUtil.getSession()) {
            // merge is potentially an update, so we need a TX
            Transaction tx = session.beginTransaction();
            session.merge(so);
            tx.commit();
        }
        ValidateSimpleObject.validate(id, 2L, "testMerge");
    }

    @Test
    public void testRefresh() {
        Long id;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.setKey("testMerge");
            simpleObject.setValue(1L);
            session.persist(simpleObject);
            id = simpleObject.getId();
            tx.commit();
        }

        SimpleObject so = ValidateSimpleObject.validate(id, 1L, "testMerge");
        // the 'so' object is detached here
        so.setValue(2L);
        try (Session session = SessionUtil.getSession()) {
            // note that refresh is a read,
            // so no TX is necessary unless an update occurs later
            session.refresh(so);
        }
        ValidateSimpleObject.validate(id, 1L, "testMerge");
        assertEquals(1L, so.getValue());
    }

    @Test
    public void testDelete() {
        Long id;
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.setKey("testMerge");
            simpleObject.setValue(1L);
            session.persist(simpleObject);
            id = simpleObject.getId();
            tx.commit();
        }

        try (Session session = SessionUtil.getSession()) {
            Transaction txn = session.beginTransaction();
            SimpleObject so2 = new SimpleObject();
            so2.setId(id);
            session.remove(so2);
            txn.commit();
        }
    }
}
