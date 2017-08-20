package com.zygimantus.apiwebas.api;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zygimantus
 */
@Service
public class TheMovieDBConsumer extends WsConsumer {

    private TmdbApi tmdbApi;

    @Override
    protected void init() {
        String tmdbMoviesApiKey = appConfig.tmdbMoviesApiKey();

        tmdbApi = new TmdbApi(tmdbMoviesApiKey);
    }

    public MovieDb getMovie(int movieId) {
        TmdbMovies movies = tmdbApi.getMovies();
        MovieDb movie = movies.getMovie(movieId, "en");

        return movie;
    }

    public MovieImages getMovieImages(int movieId) {
        TmdbMovies movies = tmdbApi.getMovies();
        MovieImages movieImages = movies.getImages(movieId, "en");

        return movieImages;
    }

    public String[] getKeys() {
        return new String[]{appConfig.tmdbMoviesApiKey()};
    }

    public void updateKeys(String key) {
        appConfig.setProperty("tmdbMoviesApiKey", key);

        init();
    }

}
