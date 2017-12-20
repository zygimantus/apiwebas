package com.zygimantus.apiwebas.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;
import javax.annotation.PostConstruct;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = ExportView.VIEW_NAME)
public final class ExportView extends VerticalLayout implements View {

        private static final long serialVersionUID = 1L;

        public static final String VIEW_NAME = "exportView";
        
        @Autowired
        private ApiwebasRepository apiwebasRepository;        

        @PostConstruct
        public void init() {
                Grid<Apiwebas> grid = new Grid<>(Apiwebas.class);

                addComponent(new VerticalLayout(new Menu()));
                addComponent(grid);

                grid.setColumns("id", "api", "resource");
                grid.setWidth("100%");
                
                grid.setItems(apiwebasRepository.findAll());
        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
        }

}
