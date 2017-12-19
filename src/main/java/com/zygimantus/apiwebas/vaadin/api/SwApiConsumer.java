package com.zygimantus.apiwebas.vaadin.api;

import com.swapi.models.Film;
import com.swapi.models.SWModelList;
import com.swapi.models.Species;
import com.swapi.sw.StarWars;
import com.swapi.sw.StarWarsApi;
import java.io.IOException;
import org.springframework.stereotype.Service;
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

        public SWModelList<Film> getFilmsList() throws InterruptedException, IOException {

                Call<SWModelList<Film>> films = api.getAllFilms(1);

                SWModelList<Film> resp = films.execute().body();

                return resp;
        }

        public SWModelList<Species> getSpeciesList() throws InterruptedException, IOException {

                Call<SWModelList<Species>> films = api.getAllSpecies(1);

                SWModelList<Species> resp = films.execute().body();

                return resp;
        }

        public Species getSpecies(int speciesId) throws InterruptedException, IOException {

                Call<Species> call = api.getSpecies(speciesId);

                Species species = call.execute().body();

                return species;
        }

}