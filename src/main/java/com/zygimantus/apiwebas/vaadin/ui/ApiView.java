package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;

/**
 *
 * @author Zygimantus
 */
public abstract class ApiView extends VerticalLayout implements View {

    @PostConstruct
    public void init() {
        addComponent(new VerticalLayout(new Menu()));
    }

}
