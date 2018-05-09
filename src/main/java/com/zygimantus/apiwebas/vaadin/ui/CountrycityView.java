package com.zygimantus.apiwebas.vaadin.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.zygimantus.apiwebas.vaadin.model.Countrycity;
import com.zygimantus.apiwebas.vaadin.model.Resource;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = CountrycityView.VIEW_NAME)
public final class CountrycityView extends ApiView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "countrycity";

	private Grid<Countrycity> grid;

	@Override
	public void init() {

		super.init();

		grid = new Grid<>(Countrycity.class);

		addComponent(grid);

		grid.addItemClickListener(e -> {
			CountryCityForm form = new CountryCityForm();
			Countrycity entity = e.getItem();

			List<String> result = apiwebasRepository.findByCountry(entity.getCountry());

			entity.setCities(result);
			form.setEntity(entity);
			form.open();
		});

		grid.setWidth("100%");
		grid.setColumns(Resource.COUNTRYCITY.getColumnIds());

	}

	@Override
	public void enter(ViewChangeEvent event) {

		// read json file data to String
		byte[] jsonData;
		try {

			jsonData = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("data/countrycity.json"));

			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			// convert json string to object
			Countrycity[] emp = objectMapper.readValue(jsonData, Countrycity[].class);

			List<Countrycity> list = Arrays.asList(emp);

			grid.setItems(list);

			save(list, Resource.COUNTRYCITY);

		} catch (IOException ex) {
			Logger.getLogger(CountrycityView.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private class CountryCityForm extends AbstractForm<Countrycity> {

		private static final long serialVersionUID = 1L;

		private TextField title;

		public CountryCityForm() {
			super(Countrycity.class);
		}

		@Override
		protected Component createContent() {
			setEnabled(false);

			title = new TextField("Title");

			MVerticalLayout layout = new MVerticalLayout(title);

			for (Object obj : getEntity().getCities()) {
				TextField tf = new TextField("City #" + getEntity().getCities().indexOf(obj));
				tf.setValue(obj.toString());
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
