package com.zygimantus.apiwebas.api;

import com.swapi.models.Film;
import com.swapi.models.SWModelList;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/swapi")
public class SwApiController extends ApiController<SWModelList> {

    @Autowired
    private SwApiConsumer swApiConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected SWModelList page() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "films", method = GET)
    protected SWModelList<Film> films() throws InterruptedException, IOException {

        SWModelList<Film> films = swApiConsumer.getFilms();

        return films;
    }

}
