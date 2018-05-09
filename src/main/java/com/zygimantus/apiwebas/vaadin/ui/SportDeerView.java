package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.model.SportDeerAccessToken;
import com.zygimantus.apiwebas.vaadin.model.BaseCountry;
import com.zygimantus.apiwebas.vaadin.model.Doc;
import com.zygimantus.apiwebas.vaadin.model.FootballCountries;
import com.zygimantus.apiwebas.vaadin.model.RegionalBloc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SportDeerView.VIEW_NAME)
public final class SportDeerView extends ApiView {

    private static final long serialVersionUID = 1L;

    private static final String SPORTDEER_ACCESS_TOKEN = "https://api.sportdeer.com/v1/accessToken?refresh_token={0}";
    private static final String FOOTBALL_COUNTRIES = "https://api.sportdeer.com/v1/countries?access_token={0}";

    public static final String VIEW_NAME = "sportDeer";

    private Grid<Doc> grid;

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${sportDeerRefreshToken}")
    private String sportDeerRefreshToken;    

    @Override
    public void init() {

        super.init();

        grid = new Grid<>(Doc.class);

        addComponent(grid);

        grid.setWidth("100%");

    }

    @Override
    public void enter(ViewChangeEvent event) {
    	
    	// getting access token
        MessageFormat mf = new MessageFormat(SPORTDEER_ACCESS_TOKEN);
        String url = mf.format(new Object[]{sportDeerRefreshToken});
    	
        String sportDeerAccessToken = restTemplate.getForEntity(url, SportDeerAccessToken.class).getBody().getNewAccessToken();

        // getting the data
        mf = new MessageFormat(FOOTBALL_COUNTRIES);
        url = mf.format(new Object[]{sportDeerAccessToken});
    	
        ResponseEntity<FootballCountries> responseEntity
                = restTemplate.getForEntity(url, FootballCountries.class);

        List list = responseEntity.getBody().getDocs();

        grid.setItems(list);

//        save(list, Resource.FOOTBALL_COUNTRIES);
    }

}
