package com.zygimantus.apiwebas.vaadin.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.zygimantus.apiwebas.vaadin.model.Countrycity;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = CountrycityView.VIEW_NAME)
public final class CountrycityView extends ApiView {

    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "countrycity";

    private Grid<Countrycity> grid;

    @Override
    public void init() {

        super.init();

        grid = new Grid<>(Countrycity.class);

        addComponent(grid);

        grid.setWidth("100%");
        grid.setColumns(Resource.COUNTRYCITY.getColumnIds());

    }

    @Override
    public void enter(ViewChangeEvent event) {

        //read json file data to String
        byte[] jsonData;
        try {
            
            jsonData = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("data/countrycity.json"));

            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            //convert json string to object
            Countrycity[] emp = objectMapper.readValue(jsonData, Countrycity[].class);

            List list = Arrays.asList(emp);

            grid.setItems(list);

            save(list, Resource.COUNTRYCITY);

        } catch (IOException ex) {
            Logger.getLogger(CountrycityView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
