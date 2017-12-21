package com.zygimantus.apiwebas.vaadin.ui;

import com.zygimantus.apiwebas.vaadin.api.SwApiConsumer;

import com.swapi.models.Film;
import com.swapi.models.People;
import com.swapi.models.Species;
import com.swapi.models.Vehicle;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Api;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;
import com.zygimantus.apiwebas.vaadin.repo.FilmRepository;
import com.zygimantus.apiwebas.vaadin.repo.PeopleRepository;
import com.zygimantus.apiwebas.vaadin.repo.SpeciesRepository;
import com.zygimantus.apiwebas.vaadin.repo.VehiclesRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    private ApiwebasRepository apiwebasRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private VehiclesRepository vehiclesRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private SwApiConsumer swApiConsumer;

    @PostConstruct
    public void init() {

        addComponent(new VerticalLayout(new Menu()));

        TabSheet tabs = new TabSheet();

        for (Resource resource : Api.SWAPI.getResources()) {

            VerticalLayout vl = new VerticalLayout();

            switch (resource) {
                case SWAPI_FILMS:
                    Grid grid = new Grid<>(Film.class);

                    grid.setColumns("title", "episodeId", "director", "producer", "release_date");
                    grid.setWidth("100%");

                    try {
                        ArrayList<Film> films = swApiConsumer.getFilmsList().getResults();
                        grid.setItems(films);

                        filmRepository.save(films);

                        Apiwebas apiwebas = new Apiwebas(resource, true);
                        apiwebasRepository.save(apiwebas);

                    } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    vl.addComponent(grid);

                    break;
                case SWAPI_SPECIES:
                    grid = new Grid<>(Species.class);

//                                        grid.setColumns("title", "episodeId", "director", "producer", "release_date");
                    grid.setWidth("100%");

                    try {
                        ArrayList<Species> species = swApiConsumer.getSpeciesList().getResults();
                        grid.setItems(species);

                        speciesRepository.save(species);

                        Apiwebas apiwebas = new Apiwebas(resource, true);
                        apiwebasRepository.save(apiwebas);

                    } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    vl.addComponent(grid);

                    break;
                case SWAPI_VEHICLE:
                    grid = new Grid<>(Vehicle.class);

//                                        grid.setColumns("title", "episodeId", "director", "producer", "release_date");
                    grid.setWidth("100%");

                    try {
                        ArrayList<Vehicle> vehicles = swApiConsumer.getVehiclesList().getResults();
                        grid.setItems(vehicles);

                        vehiclesRepository.save(vehicles);

                        Apiwebas apiwebas = new Apiwebas(resource, true);
                        apiwebasRepository.save(apiwebas);

                    } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    vl.addComponent(grid);

                    break;
                case SWAPI_PEOPLE:
                    grid = new Grid<>(People.class);

//                                        grid.setColumns("title", "episodeId", "director", "producer", "release_date");
                    grid.setWidth("100%");

                    try {
                        ArrayList<People> people = swApiConsumer.getPeopleList().getResults();
                        grid.setItems(people);

                        peopleRepository.save(people);

                        Apiwebas apiwebas = new Apiwebas(resource, true);
                        apiwebasRepository.save(apiwebas);

                    } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    vl.addComponent(grid);

                    break;
                default:
                    break;
            }

            TabSheet.Tab tab = tabs.addTab(vl);
            tab.setCaption(resource.name());
        }
        addComponent(tabs);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

}
