package com.zygimantus.apiwebas.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.api.TheMovieDBConsumer;
import info.movito.themoviedbapi.model.MovieDb;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = MovieDBView.VIEW_NAME)
public final class MovieDBView extends VerticalLayout implements View {

        private static final long serialVersionUID = 1L;

        public static final String VIEW_NAME = "movieDB";

        @Autowired
        public MovieDBView(TheMovieDBConsumer theMovieDBConsumer) {
                Grid<MovieDb> grid = new Grid<>(MovieDb.class);

                addComponent(new VerticalLayout(new Menu()));
                addComponent(grid);

                grid.setColumns("title", "popularity", "releaseDate", "budget");
                grid.setWidth("100%");

                grid.setItems(theMovieDBConsumer.getPopularMovies(1));
        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
        }

}
