package com.zygimantus.apiwebas.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.zygimantus.apiwebas.vaadin.api.TheMovieDBConsumer;
import info.movito.themoviedbapi.model.MovieDb;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = MovieDBView.VIEW_NAME)
public final class MovieDBView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "movieDB";

    @Autowired
    private TheMovieDBConsumer theMovieDBConsumer;

    @PostConstruct
    public void init() {

        Grid<MovieDb> grid = new Grid<>(MovieDb.class);

        addComponent(new VerticalLayout(new Menu()));
        addComponent(grid);

//                grid.setColumns("title", "popularity", "releaseDate", "budget");
        grid.setWidth("100%");

        grid.setItems(theMovieDBConsumer.getPopularMovies(1));

        grid.addItemClickListener(e -> {
            MovieDbForm form = new MovieDbForm();
            form.setEntity(theMovieDBConsumer.getMovie(e.getItem().getId()));
            form.open();
        });
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

    private class MovieDbForm extends AbstractForm<MovieDb> {

        private TextField title;

        public MovieDbForm() {
            super(MovieDb.class);
        }

        @Override
        protected Component createContent() {
            setEnabled(false);

            title = new TextField("Title");

            MVerticalLayout layout = new MVerticalLayout(title);
            Field[] fields = MovieDb.class.getDeclaredFields();
//            List<Component> comps = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                TextField tf = new TextField(name);
                try {
                    tf.setValue((String) field.get(getEntity()));
                } catch (IllegalArgumentException | IllegalAccessException | ClassCastException | NullPointerException ex) {
                    Logger.getLogger(MovieDBView.class.getName()).log(Level.SEVERE, null, ex);
                }
                layout.add(tf);
            }

            return layout;
        }

        @Override
        protected void save(ClickEvent e) {
        }

        public Window open() {
            final Window window = super.openInModalPopup();
            window.setWidth("50%");
            window.setCaption("");

            return window;
        }
    }
}
