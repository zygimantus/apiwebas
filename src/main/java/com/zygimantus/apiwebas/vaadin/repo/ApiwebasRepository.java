package com.zygimantus.apiwebas.vaadin.repo;

import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public interface ApiwebasRepository extends JpaRepository<Apiwebas, Integer>  {
}
