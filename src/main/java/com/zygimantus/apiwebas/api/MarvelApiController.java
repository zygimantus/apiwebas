package com.zygimantus.apiwebas.api;

import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.Format;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.karumi.marvelapiclient.model.SeriesQuery;
import com.zygimantus.apiwebas.model.DataTablesRequest;
import com.zygimantus.apiwebas.model.JsonResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/marvel")
public class MarvelApiController extends ApiController<MarvelResponse> {

    @Autowired
    private MarvelApiConsumer marvelApiConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected MarvelResponse page() {

        return new MarvelResponse();
    }

    @RequestMapping("settings/{public}/{private}")
    public JsonResponse settings(@PathVariable("public") String publicKey, @PathVariable("private") String privateKey) {

        marvelApiConsumer.updateKeys(publicKey, privateKey);

        JsonResponse jr = new JsonResponse(HttpStatus.OK);

        return jr;
    }

    @RequestMapping(value = "characters", method = POST)
    protected MarvelResponse characters(@RequestBody DataTablesRequest dtr) throws IOException, MarvelApiException {

        MarvelResponse marvelResponse = marvelApiConsumer.getCharacters(dtr);

        return marvelResponse;
    }

    @RequestMapping(value = "comics", method = POST)
    protected MarvelResponse comics(@RequestBody DataTablesRequest dtr) throws IOException, MarvelApiException {

        MarvelResponse marvelResponse = marvelApiConsumer.getComics(dtr);

        return marvelResponse;
    }

    @RequestMapping(value = "series", method = POST)
    protected MarvelResponse series(@RequestBody DataTablesRequest dtr) throws IOException, MarvelApiException {

        MarvelResponse marvelResponse = marvelApiConsumer.getSeries(dtr);

        return marvelResponse;
    }

    @RequestMapping("characters/{id}")
    protected MarvelResponse character(@PathVariable("id") String id) throws IOException, MarvelApiException {

        MarvelResponse marvelResponse = marvelApiConsumer.getCharacter(id);

        return marvelResponse;
    }

    @RequestMapping("comics/{id}")
    protected MarvelResponse comic(@PathVariable("id") String id) throws IOException, MarvelApiException {

        MarvelResponse marvelResponse = marvelApiConsumer.getComics(id);

        return marvelResponse;
    }

    @RequestMapping(value = "comics/formats", method = GET)
    protected Format[] comicsFormats() throws IOException, MarvelApiException {

        Format[] formats = Format.values();

        return formats;
    }

    @RequestMapping(value = "series/types", method = GET)
    protected SeriesQuery.SeriesType[] seriesTypes() throws IOException, MarvelApiException {

        SeriesQuery.SeriesType[] formats = SeriesQuery.SeriesType.values();

        return formats;
    }

}
