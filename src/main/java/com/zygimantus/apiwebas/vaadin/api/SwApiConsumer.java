package com.zygimantus.apiwebas.vaadin.api;

import com.swapi.models.Film;
import com.swapi.models.People;
import com.swapi.models.SWModelList;
import com.swapi.models.Species;
import com.swapi.models.Vehicle;
import com.swapi.sw.StarWars;
import com.swapi.sw.StarWarsApi;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.ui.SwapiView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import retrofit2.Call;

/**
 *
 * @author Zygimantus
 */
@Service
public class SwApiConsumer extends WsConsumer {

    private StarWars api;

    @Override
    protected void init() {
        StarWarsApi.init();
        api = StarWarsApi.getApi();
    }

    public SWModelList<Film> getFilmsList(int page) throws InterruptedException, IOException {

        Call<SWModelList<Film>> films = api.getAllFilms(page);

        SWModelList<Film> resp = films.execute().body();

        return resp;
    }

    public SWModelList<Species> getSpeciesList(int page) throws InterruptedException, IOException {

        Call<SWModelList<Species>> films = api.getAllSpecies(page);

        SWModelList<Species> resp = films.execute().body();

        return resp;
    }

    public SWModelList<Vehicle> getVehiclesList(int page) throws InterruptedException, IOException {

        Call<SWModelList<Vehicle>> films = api.getAllVehicles(page);

        SWModelList<Vehicle> resp = films.execute().body();

        return resp;
    }

    public SWModelList<People> getPeopleList(int page) throws InterruptedException, IOException {

        Call<SWModelList<People>> films = api.getAllPeople(page);

        SWModelList<People> resp = films.execute().body();

        return resp;
    }

    public Species getSpecies(int speciesId) throws InterruptedException, IOException {

        Call<Species> call = api.getSpecies(speciesId);

        Species species = call.execute().body();

        return species;
    }

    public ArrayList<Object> getFullList(Resource resource) {
        int page = 1;
        SWModelList<?> list;
        ArrayList<Object> resultList = new ArrayList<>();

        try {
            do {
                switch (resource) {
                    case SWAPI_FILMS:
                        list = getFilmsList(page);
                        break;
                    case SWAPI_SPECIES:
                        list = getSpeciesList(page);
                        break;
                    case SWAPI_PEOPLE:
                        list = getPeopleList(page);
                        break;
                    case SWAPI_VEHICLE:
                        list = getVehiclesList(page);
                        break;
                    default:
                        list = null;
                        break;
                }
                resultList.addAll(list.getResults());
                page++;
            } while (!StringUtils.isEmpty(list.getNext()));

        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(SwapiView.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultList;

    }

}
