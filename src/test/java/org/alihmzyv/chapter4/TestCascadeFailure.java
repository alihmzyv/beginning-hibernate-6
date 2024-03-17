package org.alihmzyv.chapter4;

import org.alihmzyv.chapter1.hibernate.Message;
import org.alihmzyv.chapter4.entity.Email;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

public class TestCascadeFailure {
    @Test
    public void testCascadeFailure() {
        try (Session session = SessionUtil.getSession()) {
            Transaction txn = session.beginTransaction();

            Email email = new Email();
            email.setTitle("CASCADE TITLE");
            Message message = new Message();
            message.setText("CASCADE TEXT");
            email.setMessage(message);
            session.persist(email);
            txn.commit();
        }
    }
}
