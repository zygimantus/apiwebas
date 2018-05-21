package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import com.zygimantus.apiwebas.vaadin.api.SwApiConsumer;
import com.zygimantus.apiwebas.vaadin.model.Api;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SwapiView.VIEW_NAME)
public final class SwapiView extends ApiView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "swapi";

	@Autowired
	private SwApiConsumer swApiConsumer;

	@Override
	public void init() {

		super.init();
	}

	@Override
	protected void initTabs() {
		for (Resource resource : Api.SWAPI.getResources()) {

			ResourceTab st = new ResourceTab(resource);

			TabSheet.Tab tab = tabs.addTab(st);
			tab.setCaption(resource.name());
		}
	}

	@Override
	protected ArrayList<?> getResourceData(Resource resource) {
		ArrayList<?> list = swApiConsumer.getFullList(resource);
		return list;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}

}
