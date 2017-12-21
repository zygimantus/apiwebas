package com.zygimantus.apiwebas.vaadin.repo;

import com.swapi.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public interface FilmRepository extends JpaRepository<Film, Integer>  {
}
