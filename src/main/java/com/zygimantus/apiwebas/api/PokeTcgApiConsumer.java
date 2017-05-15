package com.zygimantus.apiwebas.api;

import com.doeiqts.pokemon.tcg.domain.Card;
import com.doeiqts.pokemon.tcg.sdk.CardFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zygimantus
 */
@Service
public class PokeTcgApiConsumer extends WsConsumer {

    CardFactory cardFactory = new CardFactory();

    @Override
    protected void init() {
    }

    public Card getCard(String id) {
        Card card = cardFactory.getCard(id);

        return card;
    }
}
