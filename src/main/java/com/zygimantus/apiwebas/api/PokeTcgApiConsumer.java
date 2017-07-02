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

    private CardFactory cardFactory;

    @Override
    protected void init() {
        cardFactory = new CardFactory();
    }

    public Card getCard(String id) {
        Card card = cardFactory.getCard(id);

        return card;
    }
}
