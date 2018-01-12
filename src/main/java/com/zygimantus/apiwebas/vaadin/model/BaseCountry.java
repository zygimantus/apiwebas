package com.zygimantus.apiwebas.vaadin.model;

import java.util.List;
import lombok.Data;

/**
 *
 * @see
 * https://github.com/fayder/restcountries/blob/master/src/main/java/eu/fayder/restcountries/domain/BaseCountry.java
 */
@Data
public class BaseCountry {

    protected String name;

    private List<String> topLevelDomain;

    protected String alpha2Code;

    private String alpha3Code;

    private List<String> callingCodes;

    protected String capital;

    private List<String> altSpellings;

    protected String region;

    protected String subregion;

    protected Integer population;

    private List<Double> latlng;

    private String demonym;

    private Double area;

    protected Double gini;

    private List<String> timezones;

    protected List<String> borders;

    protected String nativeName;

    private String numericCode;

}
