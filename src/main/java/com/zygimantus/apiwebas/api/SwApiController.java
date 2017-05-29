package com.zygimantus.apiwebas.api;

import com.swapi.models.Film;
import com.swapi.models.SWModelList;
import com.zygimantus.apiwebas.ApiwebasRepository;
import com.zygimantus.apiwebas.model.Api;
import com.zygimantus.apiwebas.model.Apiwebas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Autowired
    private ApiwebasRepository apiwebasRepository;

    @Override
    protected void init() {
    }

    @Override
    protected SWModelList page() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "films", method = RequestMethod.GET)
    protected SWModelList<Film> films() throws InterruptedException, IOException {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

//        List<Apiwebas> apiwebases = session.createNamedQuery("findApiwebasByCategory", Apiwebas.class)
        List<Apiwebas> apiwebases = session.createQuery("FROM Apiwebas WHERE category = :category", Apiwebas.class)
                .setParameter("category", "films")
                .getResultList();

        transaction.commit();

        List<Film> films;
        // check if data is already in db, if not - make request to web server
        if (apiwebases.isEmpty()) {

            LOGGER.debug("was empty");

            films = storeFilms();
        } else {

            LOGGER.debug("was not empty");

            films = session.createQuery("FROM Film").getResultList();
        }
        
        SWModelList sWModelList = new SWModelList();
        sWModelList.count = films.size();
        sWModelList.results = (ArrayList) films;

        return sWModelList;
    }

    @RequestMapping(value = "films", method = RequestMethod.PUT)
    protected List<Film> storeFilms() throws InterruptedException, IOException {

        SWModelList<Film> modelList = swApiConsumer.getFilmsList();

        ArrayList<Film> films = modelList.results;

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        films.forEach((film) -> {
            session.merge(film);
        });

        transaction.commit();

        Apiwebas apiwebas = new Apiwebas();
        apiwebas.setApi(Api.SWAPI);
        // TODO also use enum here.
        apiwebas.setCategory("films");
        apiwebas.setLoaded(true);

        apiwebasRepository.save(apiwebas);

        return films;
    }

}
