package com.zygimantus.apiwebas.api;

import com.zygimantus.apiwebas.model.JsonResponse;
import info.movito.themoviedbapi.model.core.IdElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/tmdb")
public class TheMovieDBController extends ApiController<IdElement> {

    @Autowired
    private TheMovieDBConsumer theMovieDBConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected IdElement page() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping("settings")
    public String[] settings() {

        String[] keys = theMovieDBConsumer.getKeys();

        return keys;
    }

    @RequestMapping("settings/{key}")
    public JsonResponse settings(@PathVariable("key") String key) {

        JsonResponse jr = new JsonResponse(HttpStatus.OK);

        return jr;
    }

    @RequestMapping(value = "movie/{movieId}", method = RequestMethod.GET)
    protected IdElement movie(@PathVariable int movieId) {
        return theMovieDBConsumer.getMovie(movieId);
    }

    @RequestMapping(value = "movieImage/{movieId}", method = RequestMethod.GET)
    protected IdElement movieImage(@PathVariable int movieId) {
        return theMovieDBConsumer.getMovieImages(movieId);
    }

}
