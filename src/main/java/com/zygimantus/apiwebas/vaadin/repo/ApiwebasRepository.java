package com.zygimantus.apiwebas.vaadin.repo;

import com.zygimantus.apiwebas.vaadin.model.Apiwebas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Zygimantus
 */
@Repository
public interface ApiwebasRepository extends JpaRepository<Apiwebas, Integer>  {
	
	  @Query(value = "select s.city from COUNTRYCITY_CITIES s left join CC_COUNTRY c on s.COUNTRY_ID = c.COUNTRY_ID where c.COUNTRY= ?1", nativeQuery = true)
	  List<String> findByCountry(String country);	
}
