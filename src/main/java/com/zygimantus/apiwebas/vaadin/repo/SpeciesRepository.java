package com.zygimantus.apiwebas.vaadin.repo;

import com.swapi.models.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public interface SpeciesRepository extends JpaRepository<Species, Integer>  {
}
