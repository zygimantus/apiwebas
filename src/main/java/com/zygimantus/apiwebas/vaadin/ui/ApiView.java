package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Zygimantus
 */
public abstract class ApiView extends VerticalLayout implements View {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private ApiwebasRepository apiwebasRepository;

    @PostConstruct
    public void init() {
        addComponent(new VerticalLayout(new Menu()));
    }

    protected void save(List list, Resource resource) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        list.forEach((film) -> {
            session.merge(film);
        });

        transaction.commit();

        Apiwebas apiwebas = new Apiwebas(resource, true);
        apiwebasRepository.save(apiwebas);
    }

}
