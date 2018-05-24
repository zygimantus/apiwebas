package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.model.BaseCountry;
import com.zygimantus.apiwebas.vaadin.model.RegionalBloc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
    private static final String COUNTRIES_IN_REGIONAL_BLOCK_URL = "https://restcountries.eu/rest/v2/regionalbloc/{0}";

    public static final String VIEW_NAME = "restCountries";

    private Grid<BaseCountry> grid;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void init() {

        super.init();

        ComboBox<RegionalBloc> comboBox = new ComboBox<>("Regional Bloc");
        comboBox.setWidth("100%");

        ClassLoader classLoader = Menu.class.getClassLoader();
        File file = new File(classLoader.getResource("data/regional_bloc.csv").getFile());
        String line;
        String cvsSplitBy = ",";
        List<RegionalBloc> listOfRb = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] regionalBloc = line.split(cvsSplitBy);
                listOfRb.add(RegionalBloc.builder().acronym(regionalBloc[0]).name(regionalBloc[1]).build());
            }

        } catch (IOException e) {
        }
        comboBox.setItems(listOfRb);

        comboBox.setItemCaptionGenerator(RegionalBloc::getName);
        comboBox.addValueChangeListener(l -> {
            MessageFormat mf = new MessageFormat(COUNTRIES_IN_REGIONAL_BLOCK_URL);
            String url = mf.format(new Object[]{l.getValue().getAcronym()});

            ResponseEntity<BaseCountry[]> responseEntity
                    = restTemplate.getForEntity(url, BaseCountry[].class);

            List<BaseCountry> list = Arrays.asList(responseEntity.getBody());

            grid.setItems(list);
        });

        addComponent(comboBox);

        grid = new Grid<>(BaseCountry.class);

        addComponent(grid);

        grid.setWidth("100%");

    }

    @Override
    public void enter(ViewChangeEvent event) {
        ResponseEntity<BaseCountry[]> responseEntity
                = restTemplate.getForEntity(ALL_COUNTRIES_URL, BaseCountry[].class);

        List<BaseCountry> list = Arrays.asList(responseEntity.getBody());

        grid.setItems(list);

        save(list, Resource.RC_COUNTRIES);
    }

	@Override
	protected void initTabs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<?> getResourceData(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
