package com.zygimantus.apiwebas.api;

import com.deckofcards.DeckOfCards;
import com.deckofcards.DeckOfCardsApi;
import com.deckofcards.DeckResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author Zygimantus
 */
@Service
public class DeckOfCardsConsumer extends WsConsumer {

    private DeckOfCards api;

    @Override
    protected void init() {
        DeckOfCardsApi.init();
        api = DeckOfCardsApi.getApi();
    }

    public DeckResponse getDeck() throws InterruptedException, IOException {

        Call<DeckResponse> newDeck = api.createNewDeck();

        Response<DeckResponse> resp = newDeck.execute();

        return resp.body();
    }

    public DeckResponse drawCard(String deckId, int count) throws InterruptedException, IOException {

        Call<DeckResponse> newDeck = api.drawCard(deckId, count);

        Response<DeckResponse> resp = newDeck.execute();

        return resp.body();
    }

}
