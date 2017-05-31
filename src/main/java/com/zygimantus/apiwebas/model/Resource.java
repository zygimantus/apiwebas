package com.zygimantus.apiwebas.model;

import com.karumi.marvelapiclient.model.CharacterDto;
import com.swapi.models.Film;
import com.swapi.models.People;
import com.swapi.models.Species;

/**
 *
 * @author Zygimantus
 */
public enum Resource {

    SWAPI_FILMS(Api.SWAPI, Film.class),
    SWAPI_SPECIES(Api.SWAPI, Species.class),
    SWAPI_PEOPLE(Api.SWAPI, People.class),
    MARVEL_CHARACTERS(Api.MARVEL, CharacterDto.class);

    private final Api api;
    private final Class<?> aClass;

    private Resource(Api api, Class<?> aClass) {
        this.api = api;
        this.aClass = aClass;
    }

    public Api getApi() {
        return api;
    }

    public Class<?> getaClass() {
        return aClass;
    }

}
