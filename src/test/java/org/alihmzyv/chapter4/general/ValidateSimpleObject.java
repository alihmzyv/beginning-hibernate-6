package org.alihmzyv.chapter4.general;

import org.alihmzyv.chapter4.entity.SimpleObject;
import org.alihmzyv.util.hibernate.SessionUtil;
import org.hibernate.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateSimpleObject {
    public static SimpleObject validate(
            Long id,
            Long expectedValue,
            String expectedKey
    ) {
        SimpleObject so = new SimpleObject();
        try (Session session = SessionUtil.getSession()) {
            so = session.getReference(SimpleObject.class, id);
            assertEquals(so.getKey(), expectedKey);
            assertEquals(so.getValue(), expectedValue);
        }
        return so;
    }
}
