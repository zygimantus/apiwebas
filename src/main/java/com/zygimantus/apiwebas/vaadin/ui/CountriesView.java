package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import eu.fayder.restcountries.domain.BaseCountry;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = CountriesView.VIEW_NAME)
public final class CountriesView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    private static final String ALL_COUNTRIES_URL = "https://restcountries.eu/rest/v2/all";

    public static final String VIEW_NAME = "countries";

    @PostConstruct
    public void init() {

        Grid<BaseCountry> grid = new Grid<>(BaseCountry.class);

        addComponent(new VerticalLayout(new Menu()));
        addComponent(grid);

        grid.setWidth("100%");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BaseCountry[]> responseEntity = restTemplate.getForEntity(ALL_COUNTRIES_URL, BaseCountry[].class);

        grid.setItems(Arrays.asList(responseEntity.getBody()));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

}
