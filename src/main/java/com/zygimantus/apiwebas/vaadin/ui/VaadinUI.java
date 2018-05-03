package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Zygimantus
 */
@SpringUI
@SpringViewDisplay
@Theme("valo")
public class VaadinUI extends UI {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SpringNavigator navigator;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout mainLayout = new VerticalLayout();

        LoginView loginView = new LoginView(userRepository);

        mainLayout.addComponent(loginView);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(loginView, Alignment.MIDDLE_CENTER);

        setContent(mainLayout);

        navigator.setErrorView(ErrorView.class);

        navigator.navigateTo("login");
    }

}
