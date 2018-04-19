package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.faintedge.mangaedenclient.Manga;
import net.faintedge.mangaedenclient.MangaDetails;
import net.faintedge.mangaedenclient.MangaEdenClient;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = MangaEdenView.VIEW_NAME)
public final class MangaEdenView extends ApiView {

    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "mangaEden";

    private Grid<Manga> grid;

    private final MangaEdenClient client = new MangaEdenClient();

    @Override
    public void init() {

        super.init();

        grid = new Grid<>(Manga.class);

        addComponent(grid);

        grid.setWidth("100%");

    }

    @Override
    public void enter(ViewChangeEvent event) {

        try {

            List<Manga> list = client.getMangaList();

            grid.setItems(list);

            grid.addItemClickListener(e -> {
                MovieDbForm form = new MovieDbForm();
                try {
                    form.setEntity(client.getMangaDetails(e.getItem().getId()));
                } catch (IOException ex) {
                    Logger.getLogger(MangaEdenView.class.getName()).log(Level.SEVERE, null, ex);
                }
                form.open();
            });

            // very large set of data...
//            save(list, Resource.MANGAEDEN);
        } catch (IOException ex) {
            Logger.getLogger(MangaEdenView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class MovieDbForm extends AbstractForm<MangaDetails> {

        private TextField alias;

        public MovieDbForm() {
            super(MangaDetails.class);
        }

        @Override
        protected Component createContent() {
            setEnabled(false);

            alias = new TextField("Alias");

            MVerticalLayout layout = new MVerticalLayout(alias);
            Field[] fields = MangaDetails.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                TextField tf = new TextField(name);
                try {
                    tf.setValue(field.get(getEntity()).toString());
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
