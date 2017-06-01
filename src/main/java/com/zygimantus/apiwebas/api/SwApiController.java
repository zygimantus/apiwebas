package com.zygimantus.apiwebas.api;

import com.swapi.models.Film;
import com.swapi.models.SWModelList;
import com.swapi.models.Species;
import com.zygimantus.apiwebas.ApiwebasRepository;
import com.zygimantus.apiwebas.model.Apiwebas;
import com.zygimantus.apiwebas.model.DataTablesRequest;
import com.zygimantus.apiwebas.model.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = "films", method = {RequestMethod.GET, RequestMethod.POST})
    protected SWModelList<Film> films(@RequestBody DataTablesRequest dtr) throws InterruptedException, IOException {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

//        List<Apiwebas> apiwebases = session.createNamedQuery("findApiwebasByCategory", Apiwebas.class)
        List<Apiwebas> apiwebases = session.createQuery("FROM Apiwebas WHERE resource = :resource", Apiwebas.class)
                .setParameter("resource", Resource.SWAPI_FILMS)
                .getResultList();

        transaction.commit();

        List<Film> films;
        // check if data is already in db, if not - make request to web server
        if (apiwebases.isEmpty()) {

            LOGGER.debug("was empty");

            films = storeFilms();
        } else {

            LOGGER.debug("was not empty");

            String string = "FROM Film";
            if (!dtr.getOrders().isEmpty()) {
                int columnId = dtr.getOrders().get(0).getColumn();
                String columnString = "";
                switch (columnId) {
                    case 1:
                        columnString = "FILM_ID";
                        break;
                    case 2:
                        columnString = "TITLE";
                        break;
                    case 3:
                        columnString = "DIRECTOR";
                        break;
                    case 4:
                        columnString = "PRODUCER";
                        break;
                    case 5:
                        columnString = "RELEASE_DATE";
                        break;
                }
                String sortOrder = dtr.getOrders().get(0).getDir();
                string = String.format("FROM Film ORDER by %s %s", columnString, sortOrder);
            }

            films = session.createQuery(string).getResultList();
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

        Apiwebas apiwebas = new Apiwebas(Resource.SWAPI_FILMS, true);

        apiwebasRepository.save(apiwebas);

        return films;
    }

    @RequestMapping(value = "species", method = RequestMethod.PUT)
    protected ArrayList<Species> storeSpecies() throws InterruptedException, IOException {

        SWModelList<Species> modelList = swApiConsumer.getSpeciesList();

        ArrayList<Species> species = modelList.results;

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        species.forEach((film) -> {
            session.merge(film);
        });

        transaction.commit();

        Apiwebas apiwebas = new Apiwebas(Resource.SWAPI_SPECIES, true);

        apiwebasRepository.save(apiwebas);

        return species;
    }

    @RequestMapping(value = "species/{speciesId}", method = RequestMethod.GET)
    protected Species storeSpecies(@PathVariable int speciesId) throws InterruptedException, IOException {

        Species species = swApiConsumer.getSpecies(speciesId);

//        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        species.forEach((film) -> {
//            session.merge(film);
//        });
//
//        transaction.commit();
//        Apiwebas apiwebas = new Apiwebas(Resource.SWAPI_SPECIES, true);
//        apiwebasRepository.save(apiwebas);
        return species;
    }

}
