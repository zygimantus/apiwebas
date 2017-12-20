package com.zygimantus.apiwebas.vaadin.ui;

import com.zygimantus.apiwebas.vaadin.api.SwApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import com.swapi.models.Film;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.BeanUtil;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SwapiView.VIEW_NAME)
public final class SwapiView extends VerticalLayout implements View {

        private static final long serialVersionUID = 1L;

        public static final String VIEW_NAME = "swapi";

        @Autowired
        public SwapiView(SwApiConsumer swApiConsumer) {
                Grid<Film> grid = new Grid<>(Film.class);

                addComponent(new VerticalLayout(new Menu()));
                addComponent(grid);

                grid.setColumns("title", "episodeId", "director", "producer", "release_date");
                grid.setWidth("100%");

                try {
                        ArrayList<Film> films = swApiConsumer.getFilmsList().getResults();
                        grid.setItems(films);
                        
                        EntityManagerFactory entityManagerFactory = BeanUtil.getBean(EntityManagerFactory.class);

                        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

                        Session session = sessionFactory.openSession();
                        Transaction transaction = session.beginTransaction();

                        films.forEach((film) -> {
                                session.merge(film);
                        });

                        transaction.commit();

                        Apiwebas apiwebas = new Apiwebas(Resource.SWAPI_FILMS, true);
                        
                        ApiwebasRepository apiwebasRepository = BeanUtil.getBean(ApiwebasRepository.class);

                        apiwebasRepository.save(apiwebas);

                } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
        }

}
