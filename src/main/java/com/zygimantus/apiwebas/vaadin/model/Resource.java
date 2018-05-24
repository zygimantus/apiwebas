package com.zygimantus.apiwebas.vaadin.model;

import java.util.HashMap;

import com.swapi.models.Film;
import com.swapi.models.People;
import com.swapi.models.Species;
import com.swapi.models.Vehicle;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.zygimantus.sportdeerclient.SportDeerDocCountries;
import com.zygimantus.sportdeerclient.SportDeerDocFixtures;
import com.zygimantus.sportdeerclient.SportDeerDocLeagues;
import com.zygimantus.thegamesdb.model.GamesListGame;
import com.zygimantus.thegamesdb.model.PlatformListPlatform;

import lombok.Getter;
import net.faintedge.mangaedenclient.Manga;

/**
 *
 * @author Zygimantus
 */
@Getter
public enum Resource {

    SWAPI_FILMS(Api.SWAPI, Film.class, false, null, "title", "episodeId", "director", "producer", "release_date"),
    SWAPI_SPECIES(Api.SWAPI, Species.class, false, null, ""),
    SWAPI_VEHICLE(Api.SWAPI, Vehicle.class, false, null,""),
    SWAPI_PEOPLE(Api.SWAPI, People.class, false, null, ""),
    RC_COUNTRIES(Api.REST_COUNTRIES, BaseCountry.class, false, null, ""),
    COUNTRYCITY(Api.COUNTRYCITY, Countrycity.class, false, null, "country"),
    FAVQS(Api.FAVQS, ListQuotes.class, false, null, ""),
    SPORTDEER_COUNTRIES(Api.SPORTDEER, SportDeerDocCountries.class, false, null, ""),
    SPORTDEER_FIXTURES(Api.SPORTDEER, SportDeerDocFixtures.class, true, null, ""),
    SPORTDEER_LEAGUES(Api.SPORTDEER, SportDeerDocLeagues.class, true, null, ""),
    GAMESDB_PLATFROMSLIST(Api.GAMESDB, PlatformListPlatform.class, false, null, ""),
    GAMESDB_GAMESLIST(Api.GAMESDB, GamesListGame.class, false,
    		new HashMap<String, Component>()
    {
        {
            put("textField", new TextField());
        }
    }, ""),
    MANGAEDEN(Api.MANGAEDEN, Manga.class, false, null, "");
//    MARVEL_CHARACTERS(Api.MARVEL, CharacterDto.class);

    private final Api api;
    private final Class<?> aClass;
    private final String[] columnIds;
    private final boolean usePagination;
    private final HashMap config;

    private Resource(Api api, Class<?> aClass, boolean usePagination, HashMap config, String... columnIds) {
        this.api = api;
        this.aClass = aClass;
        this.usePagination = usePagination;
        this.config = config;
        this.columnIds = columnIds;
    }

}
