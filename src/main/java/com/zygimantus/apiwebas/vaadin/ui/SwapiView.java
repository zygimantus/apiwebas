package com.zygimantus.apiwebas.vaadin.ui;

import com.zygimantus.apiwebas.vaadin.api.SwApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import com.swapi.models.Film;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

                addComponent(grid);

                grid.setColumns("title", "episodeId", "director", "producer", "release_date");
                grid.setWidth("100%");

                try {
                        grid.setItems(swApiConsumer.getFilmsList().getResults());
                } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
        }

}
