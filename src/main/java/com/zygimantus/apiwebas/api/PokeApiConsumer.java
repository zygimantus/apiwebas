package com.zygimantus.apiwebas.api;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
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

    public PokemonSpecies getPokemon(int id) {
        PokemonSpecies pokemonSpecies = pokeApi.getPokemonSpecies(id);

        return pokemonSpecies;
    }

}
