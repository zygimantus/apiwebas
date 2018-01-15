package com.zygimantus.apiwebas.vaadin.model;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Zygimantus
 */
@Data
public class Countrycity {

    private String country;
    private List<String> cities = null;

}
