package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.zygimantus.sportdeerclient.Doc;
import com.zygimantus.sportdeerclient.SportDeer;
import com.zygimantus.sportdeerclient.SportDeerApi;
import com.zygimantus.sportdeerclient.SportDeerCountries;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Response;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SportDeerView.VIEW_NAME)
public final class SportDeerView extends ApiView {

    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "sportDeer";

    private Grid<Doc> grid;

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

        try {
            SportDeerApi.init();
            SportDeer api = SportDeerApi.getApi();

            Response<com.zygimantus.sportdeerclient.SportDeerAccessToken> response = api.getAccessToken(sportDeerRefreshToken).execute();

            Response<SportDeerCountries> response1 = api.getCountries(response.body().getNewAccessToken()).execute();

            List list = response1.body().getDocs();
            grid.setItems(list);

//        save(list, Resource.FOOTBALL_COUNTRIES);
        } catch (IOException ex) {
            Logger.getLogger(SportDeerView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
