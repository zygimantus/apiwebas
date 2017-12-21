package com.zygimantus.apiwebas.vaadin.repo;

import com.swapi.models.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public interface PeopleRepository extends JpaRepository<People, Integer>  {
}
