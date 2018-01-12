package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import eu.fayder.restcountries.domain.BaseCountry;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = RestCountriesView.VIEW_NAME)
public final class RestCountriesView extends ApiView {

    private static final long serialVersionUID = 1L;

    private static final String ALL_COUNTRIES_URL = "https://restcountries.eu/rest/v2/all";

    public static final String VIEW_NAME = "countries";

    @Override
    public void init() {

        super.init();

        Grid<BaseCountry> grid = new Grid<>(BaseCountry.class);

        addComponent(grid);

        grid.setWidth("100%");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BaseCountry[]> responseEntity = restTemplate.getForEntity(ALL_COUNTRIES_URL, BaseCountry[].class);

        List list = Arrays.asList(responseEntity.getBody());

        grid.setItems(list);

        save(list, Resource.RC_COUNTRIES);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

}
