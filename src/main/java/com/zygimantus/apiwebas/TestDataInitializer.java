package com.zygimantus.apiwebas;

import com.zygimantus.apiwebas.model.User;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * This is a initializing bean that inserts some test data in the database. It
 * is only active in the development profile, to see the data login with user123
 * / PAssword2 and do a search starting on 1st of January 2015.
 *
 */
@Component
public class TestDataInitializer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void init() throws Exception {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = new User("test123", "$2a$10$x9vXeDsSC2109FZfIJz.pOZ4dJ056xBpbesuMJg3jZ.ThQkV119tS", "test@email.com");

        session.persist(user);

        transaction.commit();
    }
}
