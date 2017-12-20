package com.zygimantus.apiwebas.vaadin.api;

import java.util.List;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zygimantus
 */
@Service
public class PokeApiConsumer extends WsConsumer {

        private PokeApi pokeApi;

        @Override
        protected void init() {
                pokeApi = new PokeApiClient();
        }

        public List<NamedApiResource> getPokemonList(int i, int il) {
                NamedApiResourceList pokemonSpecies = pokeApi.getPokemonList(i, il);

                return pokemonSpecies.getResults();
        }

        public PokemonSpecies getPokemon(int id) {
                PokemonSpecies pokemonSpecies = pokeApi.getPokemonSpecies(id);

                return pokemonSpecies;
        }

}
