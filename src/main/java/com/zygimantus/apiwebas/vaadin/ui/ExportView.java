package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.icons.VaadinIcons;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Zygimantus
 */
@Slf4j
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

                grid.setColumns("id", "api", "resource", "createDate");

                grid.addComponentColumn(this::buildExportButton)
                        .setCaption("Actions");

                grid.setWidth("100%");

                grid.setItems(apiwebasRepository.findAll());
        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
        }

        private Button buildExportButton(Apiwebas apiwebas) {
                Button button = new Button(VaadinIcons.DOWNLOAD);
                button.addStyleName(ValoTheme.BUTTON_SMALL);
                button.addClickListener(e -> export(apiwebas));
                return button;
        }

        private void export(Apiwebas apiwebas) {
                log.debug("Exporting: {}", apiwebas.toString());
        }

}
