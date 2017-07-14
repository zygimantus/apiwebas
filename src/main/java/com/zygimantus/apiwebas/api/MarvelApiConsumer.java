package com.zygimantus.apiwebas.api;

import com.karumi.marvelapiclient.CharacterApiClient;
import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.SeriesApiClient;
import com.karumi.marvelapiclient.model.CharacterDto;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.CharactersQuery;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.Format;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.karumi.marvelapiclient.model.OrderBy;
import com.karumi.marvelapiclient.model.SeriesCollectionDto;
import com.karumi.marvelapiclient.model.SeriesQuery;
import com.zygimantus.apiwebas.model.DataTablesRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zygimantus
 */
@Service
public class MarvelApiConsumer extends WsConsumer {

    private MarvelApiConfig marvelApiConfig;

    @Override
    protected void init() {
        String publicKey = appConfig.publicKey();
        String privateKey = appConfig.privateKey();

        marvelApiConfig = new MarvelApiConfig.Builder(publicKey, privateKey).debug().build();
    }

    public String[] getKeys() {
        return new String[]{appConfig.publicKey(), appConfig.privateKey()};
    }

    public void updateKeys(String publicKey, String privateKey) {
        appConfig.setProperty("publicKey", publicKey);
        appConfig.setProperty("privateKey", privateKey);

        init();
    }

    public MarvelResponse getCharacters(DataTablesRequest dtr) throws MarvelApiException {
        DataTablesRequest.Order order = dtr.getOrders().get(0);

        OrderBy orderBy;

        switch (order.getColumn()) {
            case 2:
                orderBy = OrderBy.NAME;
                break;
            case 4:
                orderBy = OrderBy.MODIFIED;
                break;
            default:
                orderBy = null;
                break;
        }

        CharacterApiClient characterApiClient = new CharacterApiClient(marvelApiConfig);
        CharactersQuery charactersQuery = CharactersQuery.Builder.create()
                .withLimit(dtr.getLength())
                .withOffset(dtr.getStart())
                .withNameStartWith("".equals(dtr.getSearch().getValue()) ? null : dtr.getSearch().getValue())
                .withOrderBy(orderBy, "asc".equals(order.getDir()))
                .build();

        MarvelResponse<CharactersDto> marvelResponse = characterApiClient.getAll(charactersQuery);

        return marvelResponse;
    }

    public MarvelResponse getComics(DataTablesRequest dtr) throws MarvelApiException {
        DataTablesRequest.Order order = dtr.getOrders().get(0);

        OrderBy orderBy;

        switch (order.getColumn()) {
            case 2:
                orderBy = null;
                break;
            case 5:
                orderBy = OrderBy.MODIFIED;
                break;
            default:
                orderBy = null;
                break;
        }

        // search by column values
        Map<String, String> map = new HashMap<>();
        List<DataTablesRequest.Column> columns = dtr.getColumns();
        for (DataTablesRequest.Column column : columns) {
            if (!column.getSearch().getValue().equals("")) {
                map.put(column.getData(), column.getSearch().getValue());
            }
        }

        ComicApiClient comicApiClient = new ComicApiClient(marvelApiConfig);
        ComicsQuery.Builder builder = ComicsQuery.Builder.create();

        builder.withLimit(dtr.getLength())
                .withOffset(dtr.getStart())
                .withTitleStartsWith("".equals(dtr.getSearch().getValue()) ? null : dtr.getSearch().getValue())
                .withOrderBy(orderBy, "asc".equals(order.getDir()));

        // search by format
        map.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("format")) {
                builder.withFormat(Format.valueOf(value));
            }
        });

        ComicsQuery comicQuery = builder.build();

        MarvelResponse<ComicsDto> marvelResponse = comicApiClient.getAll(comicQuery);

        return marvelResponse;
    }

    public MarvelResponse getSeries(DataTablesRequest dtr) throws MarvelApiException, MarvelApiException, MarvelApiException, MarvelApiException {

        DataTablesRequest.Order order = dtr.getOrders().get(0);

        OrderBy orderBy;

        switch (order.getColumn()) {
            case 2:
                orderBy = null;
                break;
            case 4:
                orderBy = OrderBy.MODIFIED;
                break;
            default:
                orderBy = null;
                break;
        }

        // search by column values
        Map<String, String> map = new HashMap<>();
        List<DataTablesRequest.Column> columns = dtr.getColumns();
        for (DataTablesRequest.Column column : columns) {
            if (!column.getSearch().getValue().equals("")) {
                map.put(column.getData(), column.getSearch().getValue());
            }
        }

        SeriesApiClient seriesApiClient = new SeriesApiClient(marvelApiConfig);
        SeriesQuery.Builder builder = SeriesQuery.Builder.create();

        builder.withLimit(dtr.getLength())
                .withOffset(dtr.getStart())
                .withTitleStartsWith("".equals(dtr.getSearch().getValue()) ? null : dtr.getSearch().getValue())
                .withOrderBy(orderBy, "asc".equals(order.getDir()));

        // TODO search by type
        map.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("type")) {
                builder.withSeriesType(SeriesQuery.SeriesType.valueOf(value));
            }
        });

        SeriesQuery seriesQuery = builder.build();

        MarvelResponse<SeriesCollectionDto> marvelResponse = seriesApiClient.getAll(seriesQuery);

        return marvelResponse;
    }

    public MarvelResponse getCharacter(String id) throws MarvelApiException {
        CharacterApiClient characterApiClient = new CharacterApiClient(marvelApiConfig);
        MarvelResponse<CharacterDto> marvelResponse = characterApiClient.getCharacter(id);

        return marvelResponse;
    }

    public MarvelResponse getComics(String id) throws MarvelApiException {
        ComicApiClient comicApiClient = new ComicApiClient(marvelApiConfig);
        MarvelResponse<ComicDto> marvelResponse = comicApiClient.getComic(id);

        return marvelResponse;
    }

}
