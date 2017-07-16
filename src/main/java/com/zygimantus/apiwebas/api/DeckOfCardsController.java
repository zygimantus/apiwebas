package com.zygimantus.apiwebas.api;

import com.deckofcards.DeckResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/cards")
public class DeckOfCardsController extends ApiController<DeckResponse> {

    @Autowired
    private DeckOfCardsConsumer deckOfCardsConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected DeckResponse page() {

        return new DeckResponse();
    }

    /**
     *
     * @return @throws InterruptedException
     * @throws IOException
     */
    @RequestMapping(value = "deck", method = RequestMethod.GET)
    protected DeckResponse deck() throws InterruptedException, IOException {

        DeckResponse deckResponse = deckOfCardsConsumer.getDeck();

        return deckResponse;
    }

    /**
     * Draw a card from a deck by its id.
     *
     * @param deckId
     * @param count
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @RequestMapping(value = "card/{deckId}/{count}", method = RequestMethod.GET)
    protected DeckResponse deck(@PathVariable("deckId") String deckId,
            @PathVariable("count") int count)
            throws InterruptedException, IOException {

        DeckResponse deckResponse = deckOfCardsConsumer.drawCard(deckId, count);

        return deckResponse;
    }

}
