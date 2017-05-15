package com.zygimantus.apiwebas.api;

import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/poke")
public class PokeApiController extends ApiController<PokemonSpecies> {

    @Autowired
    private PokeApiConsumer pokeApiConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected PokemonSpecies page() {
        PokemonSpecies pokemon = pokeApiConsumer.getPokemon(1);
        return pokemon;
    }

}
