package com.zygimantus.apiwebas.vaadin.model;

import com.swapi.models.Film;
import com.swapi.models.People;
import com.swapi.models.Species;
import com.swapi.models.Vehicle;
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

    SWAPI_FILMS(Api.SWAPI, Film.class, false, "title", "episodeId", "director", "producer", "release_date"),
    SWAPI_SPECIES(Api.SWAPI, Species.class, false, ""),
    SWAPI_VEHICLE(Api.SWAPI, Vehicle.class, false, ""),
    SWAPI_PEOPLE(Api.SWAPI, People.class, false, ""),
    RC_COUNTRIES(Api.REST_COUNTRIES, BaseCountry.class, false, ""),
    COUNTRYCITY(Api.COUNTRYCITY, Countrycity.class, false, "country"),
    FAVQS(Api.FAVQS, ListQuotes.class, false, ""),
    SPORTDEER_COUNTRIES(Api.SPORTDEER, SportDeerDocCountries.class, false, ""),
    SPORTDEER_FIXTURES(Api.SPORTDEER, SportDeerDocFixtures.class, true, ""),
    SPORTDEER_LEAGUES(Api.SPORTDEER, SportDeerDocLeagues.class, true, ""),
    GAMESDB_PLATFROMSLIST(Api.GAMESDB, PlatformListPlatform.class, false, ""),
    GAMESDB_GAMESLIST(Api.GAMESDB, GamesListGame.class, false, ""),
    MANGAEDEN(Api.MANGAEDEN, Manga.class, false, "");
//    MARVEL_CHARACTERS(Api.MARVEL, CharacterDto.class);

    private final Api api;
    private final Class<?> aClass;
    private final String[] columnIds;
    private final boolean usePagination;

    private Resource(Api api, Class<?> aClass, boolean usePagination, String... columnIds) {
        this.api = api;
        this.aClass = aClass;
        this.usePagination = usePagination;
        this.columnIds = columnIds;
    }

}
