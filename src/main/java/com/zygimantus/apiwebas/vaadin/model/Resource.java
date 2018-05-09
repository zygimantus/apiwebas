package com.zygimantus.apiwebas.vaadin.model;

import com.swapi.models.Film;
import com.swapi.models.People;
import com.swapi.models.Species;
import com.swapi.models.Vehicle;
import lombok.Getter;
import net.faintedge.mangaedenclient.Manga;

/**
 *
 * @author Zygimantus
 */
@Getter
public enum Resource {

    SWAPI_FILMS(Api.SWAPI, Film.class, "title", "episodeId", "director", "producer", "release_date"),
    SWAPI_SPECIES(Api.SWAPI, Species.class, ""),
    SWAPI_VEHICLE(Api.SWAPI, Vehicle.class, ""),
    SWAPI_PEOPLE(Api.SWAPI, People.class, ""),
    RC_COUNTRIES(Api.REST_COUNTRIES, BaseCountry.class, ""),
    COUNTRYCITY(Api.COUNTRYCITY, Countrycity.class, "country"),
    FAVQS(Api.FAVQS, ListQuotes.class, ""),
    FOOTBALL_COUNTRIES(Api.SPORTDEER, FootballCountries.class, ""),
    MANGAEDEN(Api.MANGAEDEN, Manga.class, "");
//    MARVEL_CHARACTERS(Api.MARVEL, CharacterDto.class);

    private final Api api;
    private final Class<?> aClass;
    private final String[] columnIds;

    private Resource(Api api, Class<?> aClass, String... columnIds) {
        this.api = api;
        this.aClass = aClass;
        this.columnIds = columnIds;
    }

}
