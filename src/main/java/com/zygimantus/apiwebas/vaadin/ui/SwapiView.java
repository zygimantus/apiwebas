package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
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

    private TabSheet tabs;

    @Override
    public void init() {

        super.init();

        tabs = new TabSheet();

        for (Resource resource : Api.SWAPI.getResources()) {

            SwapiTab swapiTab = new SwapiTab(resource);

            TabSheet.Tab tab = tabs.addTab(swapiTab);
            tab.setCaption(resource.name());
        }

        tabs.addSelectedTabChangeListener(e -> {
            Component tab = e.getTabSheet().getSelectedTab();
            if (tab instanceof SwapiTab) {
                SwapiTab st = (SwapiTab) tab;
                st.createGridAndSave();
            }
        });

        addComponent(tabs);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

    private class SwapiTab extends VerticalLayout {

		private static final long serialVersionUID = 1L;
		private final Resource resource;

        private SwapiTab(Resource resource) {
            this.resource = resource;
        }

        public void createGridAndSave() {
            ArrayList list = swApiConsumer.getFullList(resource);
            Grid grid = new Grid<>(resource.getAClass());

            String[] colIds = resource.getColumnIds();
            if (colIds.length != 1) {
                grid.setColumns(colIds);
            }
            grid.setWidth("100%");
            grid.setItems(list);

            save(list, resource);

            addComponent(grid);
        }

    }

}
