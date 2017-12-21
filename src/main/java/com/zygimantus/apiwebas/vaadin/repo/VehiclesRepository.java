package com.zygimantus.apiwebas.vaadin.repo;

import com.swapi.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public interface VehiclesRepository extends JpaRepository<Vehicle, Integer>  {
}
