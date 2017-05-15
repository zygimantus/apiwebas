package com.zygimantus.apiwebas.api;

import com.doeiqts.pokemon.tcg.domain.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/poketcg")
public class PokeTcgApiController extends ApiController<Card> {

    @Autowired
    private PokeTcgApiConsumer pokeTcgApiConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected Card page() {
        Card card = pokeTcgApiConsumer.getCard("xy8-15");
        return card;
    }

}
