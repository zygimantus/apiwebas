package com.zygimantus.apiwebas.vaadin.model;

import lombok.Getter;

/**
 *
 * @author Zygimantus
 */
@Getter
public enum Api {

//    MARVEL {
//        @Override
//        public Resource[] getResources() {
//            return new Resource[]{
//                Resource.MARVEL_CHARACTERS
//            };
//        }
//    },
    SWAPI("Swapi", "swapi") {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.SWAPI_FILMS, Resource.SWAPI_SPECIES, Resource.SWAPI_VEHICLE, Resource.SWAPI_PEOPLE,};
        }
    },
    MOVIEDB("MovieDB", "movieDB") {
        @Override
        public Resource[] getResources() {
            return new Resource[]{};
        }
    },
    REST_COUNTRIES("RestCountries", "restCountries") {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.RC_COUNTRIES,};
        }
    },
    COUNTRYCITY("Countrycity", "countrycity") {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.COUNTRYCITY,};
        }
    },
    FAVQS("FavQs", "favQs") {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.FAVQS,};
        }
    },
    MANGAEDEN("MangaEden", "mangaEden") {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.MANGAEDEN,};
        }
    };

    private final String name;
    private final String viewName;

    private Api(String name, String viewName) {
        this.name = name;
        this.viewName = viewName;
    }

    // using enum polymorphism technique
    public abstract Resource[] getResources();
}
