package com.zygimantus.apiwebas.vaadin.api;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zygimantus
 */
@Service
public class TheMovieDBConsumer extends WsConsumer {

        private TmdbApi tmdbApi;

        @Value("${tmdbMoviesApiKey}")
        private String tmdbMoviesApiKey;

        @Override
        protected void init() {

                tmdbApi = new TmdbApi(tmdbMoviesApiKey);
        }

        public List<MovieDb> getPopularMovies(int page) {
                TmdbMovies movies = tmdbApi.getMovies();
                MovieResultsPage resultsPage = movies.getPopularMovies("en", page);

                return resultsPage.getResults();
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

}
