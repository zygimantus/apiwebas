package com.zygimantus.apiwebas;

import com.zygimantus.apiwebas.model.Apiwebas;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public class ApiwebasRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void save(Apiwebas apiwebas) {

        // TODO those lines below can be in abstract class also
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(apiwebas);

        transaction.commit();

    }
}
