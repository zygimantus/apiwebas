package com.zygimantus.apiwebas.api;

import com.deckofcards.DeckResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/cards")
public class DeckOfCardsController {

    @Autowired
    private DeckOfCardsConsumer deckOfCardsConsumer;

    @RequestMapping(value = "deck", method = RequestMethod.GET)
    protected DeckResponse deck() throws InterruptedException, IOException {

        DeckResponse deckResponse = deckOfCardsConsumer.getDeck();

        return deckResponse;
    }

}
