package com.zygimantus.apiwebas.vaadin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Zygimantus
 */
@Data
public class ListQuotes {

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("last_page")
    private Boolean lastPage;
    @JsonProperty("quotes")
    private List<Quote> quotes = null;

}
