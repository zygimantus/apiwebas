package com.zygimantus.apiwebas.api;

import com.swapi.models.Film;
import com.swapi.models.SWModelList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/swapi")
public class SwApiController extends ApiController<SWModelList> {

    @Autowired
    private SwApiConsumer swApiConsumer;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    protected void init() {
    }

    @Override
    protected SWModelList page() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "films", method = GET)
    protected SWModelList<Film> films() throws InterruptedException, IOException {

        SWModelList<Film> modelList = swApiConsumer.getFilmsList();

        ArrayList<Film> films = modelList.results;

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        films.forEach((film) -> {
            session.persist(film);
        });

        transaction.commit();

        return modelList;
    }

    @RequestMapping(value = "load", method = GET)
    protected List<Film> load() throws InterruptedException, IOException {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Film> films = session.createQuery("FROM Film").getResultList();

        transaction.commit();

        return films;
    }

}
