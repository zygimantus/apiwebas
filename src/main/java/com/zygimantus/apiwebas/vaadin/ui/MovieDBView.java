package com.zygimantus.apiwebas.vaadin.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.zygimantus.apiwebas.vaadin.api.TheMovieDBConsumer;
import com.zygimantus.apiwebas.vaadin.model.Consts;
import com.zygimantus.apiwebas.vaadin.model.Resource;

import info.movito.themoviedbapi.model.MovieDb;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = MovieDBView.VIEW_NAME)
public final class MovieDBView extends ApiView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "movieDB";

	@Autowired
	private TheMovieDBConsumer theMovieDBConsumer;

	private TabSheet tabs;

	@Override
	public void init() {

		super.init();

		tabs = new TabSheet();

		for (String string : Consts.MOVIE_TYPES) {

			MovieTab swapiTab = new MovieTab(string);

			TabSheet.Tab tab = tabs.addTab(swapiTab);
			tab.setCaption(string);
		}

		tabs.addSelectedTabChangeListener(e -> {
			Component tab = e.getTabSheet().getSelectedTab();
			if (tab instanceof MovieTab) {
				MovieTab st = (MovieTab) tab;
				st.createGridAndSave();
			}
		});

		addComponent(tabs);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}

	private class MovieDbForm extends AbstractForm<MovieDb> {

		private static final long serialVersionUID = 1L;

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
			// List<Component> comps = new ArrayList<>();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				TextField tf = new TextField(name);
				Class<?> clazz = field.getType();
				try {
					Object entity = field.get(getEntity());
					if (entity != null) {
						if (clazz.isAssignableFrom(String.class)) {
							tf.setValue(field.get(getEntity()).toString());
						// currently always these are null
//						} else if (clazz.isAssignableFrom(Credits.class)) {
//							Credits cred = (Credits) entity;
//							tf.setValue(cred.getCast().stream().map(i -> i.getCharacter() + ": " + i.getName())
//									.collect(Collectors.joining(",")));
//						} else if (clazz.isAssignableFrom(MoviesAlternativeTitles.class)) {
//							MoviesAlternativeTitles mat = (MoviesAlternativeTitles) entity;
//							tf.setValue(mat.getTitles().stream().map(i -> i.getCountry() + ": " + i.getTitle())
//									.collect(Collectors.joining(",")));
						}
					} else {
						tf.setValue("-");
					}
				} catch (IllegalArgumentException | IllegalAccessException | ClassCastException
						| NullPointerException ex) {
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

	private class MovieTab extends VerticalLayout {

		private static final long serialVersionUID = 1L;
		private final String type;

		private MovieTab(String type) {
			this.type = type;
		}

		public void createGridAndSave() {

			Grid<MovieDb> grid = new Grid<>(MovieDb.class);
			switch (type) {
			case "popular":
			default:
				grid.setItems(theMovieDBConsumer.getPopularMovies(1));
				break;
			case "top":
				grid.setItems(theMovieDBConsumer.getTopMovies(1));				
				break;
			case "now":
				grid.setItems(theMovieDBConsumer.getNowMovies(1));				
				break;
			}

			grid.setWidth("100%");

			// save(list, resource);

			addComponent(grid);

			grid.addItemClickListener(e -> {
				MovieDbForm form = new MovieDbForm();
				form.setEntity(theMovieDBConsumer.getMovie(e.getItem().getId()));
				form.open();
			});

		}

	}

	@Override
	protected void initTabs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<?> getResourceData(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
