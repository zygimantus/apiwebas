package com.zygimantus.apiwebas.vaadin.model;

/**
 *
 * @author Zygimantus
 */
public enum Api {

//    MARVEL {
//        @Override
//        public Resource[] getResources() {
//            return new Resource[]{
//                Resource.MARVEL_CHARACTERS
//            };
//        }
//    },
    SWAPI {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.SWAPI_FILMS, Resource.SWAPI_SPECIES, Resource.SWAPI_VEHICLE, Resource.SWAPI_PEOPLE,};
        }
    },
    REST_COUNTRIES {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.RC_COUNTRIES,};
        }
    };

    // using enum polymorphism technique
    public abstract Resource[] getResources();
}
