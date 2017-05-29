package com.zygimantus.apiwebas.model;

/**
 *
 * @author Zygimantus
 */
public enum Api {

    MARVEL {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.MARVEL_CHARACTERS
            };
        }
    },
    SWAPI {
        @Override
        public Resource[] getResources() {
            return new Resource[]{
                Resource.SWAPI_FILMS, Resource.SWAPI_PEOPLE
            };
        }
    };

    // using enum polymorphism technique
    public abstract Resource[] getResources();
}
