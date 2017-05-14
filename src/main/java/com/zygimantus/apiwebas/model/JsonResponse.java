package com.zygimantus.apiwebas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Zygimantus
 * @param <D>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse<D extends JsonResponse.Data> {

    private Integer code;

    private String status;

    private String description;

    @JsonProperty("data")
    private D data;

    public JsonResponse() {
    }

    public JsonResponse(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public JsonResponse(Integer code, String status, String description) {
        this.code = code;
        this.status = status;
        this.description = description;
    }

    public JsonResponse(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
    }

    public JsonResponse(HttpStatus httpStatus, String description) {
        this.code = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("data")
    public D getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(D data) {
        this.data = data;
    }

    public boolean hasData() {
        return data != null;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data<T> {

    }

}
