package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.zygimantus.apiwebas.vaadin.HeaderRequestInterceptor;
import com.zygimantus.apiwebas.vaadin.model.ListQuotes;
import com.zygimantus.apiwebas.vaadin.model.Quote;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = FavQsView.VIEW_NAME)
public final class FavQsView extends ApiView {

    private static final long serialVersionUID = 1L;

    private static final String LIST_QUOTES_URL = "https://favqs.com/api/quotes";

    public static final String VIEW_NAME = "favQs";

    private Grid<Quote> grid;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${favQsApiKey}")
    private String favQsApiKey;

    @Override
    public void init() {

        super.init();

        grid = new Grid<>(Quote.class);

        addComponent(grid);

        grid.setWidth("100%");

        // customization
//        column names:
//        authorPermalink
//        _private
//        upvotesCount
//        author
//        dialogue
//        downvotesCount
//        id
//        favoritesCount
//        body
//        url
//        tags
        ButtonRenderer br = new ButtonRenderer();
        br.addClickListener((event) -> {
            Quote quote = (Quote) event.getItem();
            getUI().getPage().open(quote.getUrl(), "_blank");
        });
        grid.getColumn("url").setRenderer(br);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization",
                String.format("Token token=\"%s\"", favQsApiKey)));

        restTemplate.setInterceptors(interceptors);
        ResponseEntity<ListQuotes> responseEntity
                = restTemplate.getForEntity(LIST_QUOTES_URL, ListQuotes.class);

        List list = responseEntity.getBody().getQuotes();

        grid.setItems(list);

        save(list, Resource.FAVQS);
    }

}
