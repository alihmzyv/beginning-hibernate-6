package org.alihmzyv.chapter4.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.alihmzyv.chapter1.hibernate.Message;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Entity
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String title;

    @OneToOne(cascade = CascadeType.PERSIST)
    Message message;
}
