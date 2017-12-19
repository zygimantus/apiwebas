package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.annotations.Theme;
import com.zygimantus.apiwebas.vaadin.api.SwApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import com.zygimantus.apiwebas.vaadin.repo.UserRepository;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
        private final UserRepository userRepository;

        @Autowired
        private SwApiConsumer swApiConsumer;

        private Navigator navigator;

        @Override
        protected void init(VaadinRequest request) {

                VerticalLayout mainLayout = new VerticalLayout();

                LoginView loginView = new LoginView(userRepository);

                mainLayout.addComponent(loginView);
                mainLayout.setSizeFull();
                mainLayout.setComponentAlignment(loginView, Alignment.MIDDLE_CENTER);

                setContent(mainLayout);

                SwapiView mainView = new SwapiView(swApiConsumer);

                navigator = new Navigator(this, mainLayout);

                navigator.addView(LoginView.VIEW_NAME, loginView);
                navigator.addView(SwapiView.VIEW_NAME, mainView);
                navigator.navigateTo(LoginView.VIEW_NAME);

        }

        @Autowired
        public VaadinUI(UserRepository repo) {
                this.userRepository = repo;
        }
}
