package com.zygimantus.apiwebas.api;

import com.zygimantus.apiwebas.AController;
import com.zygimantus.apiwebas.model.JsonResponse;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Zygimantus
 * @param <T>
 */
@Api
public abstract class ApiController<T> extends AController<T> {

    @ExceptionHandler(Exception.class)
    public final JsonResponse handleAllException(Exception ex) {

        // debugging
        LOGGER.error(ex);
        
        ex.printStackTrace();

        return new JsonResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getLocalizedMessage());
    }

}
