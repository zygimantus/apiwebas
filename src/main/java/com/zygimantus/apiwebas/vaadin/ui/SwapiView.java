package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.api.SwApiConsumer;
import com.zygimantus.apiwebas.vaadin.model.Api;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SwapiView.VIEW_NAME)
public final class SwapiView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "swapi";

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private ApiwebasRepository apiwebasRepository;
    @Autowired
    private SwApiConsumer swApiConsumer;

    private TabSheet tabs;

    @PostConstruct
    public void init() {

        addComponent(new VerticalLayout(new Menu()));

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
                st.save();
            }
        });

        addComponent(tabs);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

    private class SwapiTab extends VerticalLayout {

        private final Resource resource;

        private SwapiTab(Resource resource) {
            this.resource = resource;
        }

        public void save() {
            ArrayList list = swApiConsumer.getFullList(resource);
            Grid grid = new Grid<>(resource.getAClass());

            String[] colIds = resource.getColumnIds();
            if (colIds.length != 1) {
                grid.setColumns(colIds);
            }
            grid.setWidth("100%");
            grid.setItems(list);

            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            list.forEach((film) -> {
                session.merge(film);
            });

            transaction.commit();

            Apiwebas apiwebas = new Apiwebas(resource, true);
            apiwebasRepository.save(apiwebas);

            addComponent(grid);
        }

    }

}
