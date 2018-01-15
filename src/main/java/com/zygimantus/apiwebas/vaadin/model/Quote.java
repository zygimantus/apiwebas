package com.zygimantus.apiwebas.vaadin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Zygimantus
 */
@Data
public class Quote {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("dialogue")
    private Boolean dialogue;
    @JsonProperty("private")
    private Boolean _private;
    @JsonProperty("tags")
    private List<String> tags = null;
    @JsonProperty("url")
    private String url;
    @JsonProperty("favorites_count")
    private Integer favoritesCount;
    @JsonProperty("upvotes_count")
    private Integer upvotesCount;
    @JsonProperty("downvotes_count")
    private Integer downvotesCount;
    @JsonProperty("author")
    private String author;
    @JsonProperty("author_permalink")
    private String authorPermalink;
    @JsonProperty("body")
    private String body;

}
