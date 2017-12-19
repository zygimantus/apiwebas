package com.zygimantus.apiwebas.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.zygimantus.apiwebas.vaadin.model.User;
import com.zygimantus.apiwebas.vaadin.repo.UserRepository;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

@SpringView(name = LoginView.VIEW_NAME)
public final class LoginView extends CustomComponent implements View {

        private static final long serialVersionUID = 1L;

        public static final String VIEW_NAME = "login";

        public User user;

        TextField userName = new TextField("username");
        TextField password = new TextField("password");
        Button login = new Button("Login");

        Binder<User> binder = new Binder<>();

        @Autowired
        public LoginView(UserRepository userRepository) {

                // Create a panel with a caption.
                Panel loginPanel = new Panel("Login");
                loginPanel.setSizeUndefined();
                loginPanel.setWidth(null);

                // Create a layout inside the panel
                final FormLayout loginLayout = new FormLayout();
                // Add some components inside the layout
                loginLayout.addComponent(userName);
                loginLayout.addComponent(password);
                loginLayout.addComponent(login);

                setSizeUndefined();
                // Have some margin around it.
                loginLayout.setMargin(true);

                // set required indicator for text fields
                userName.setRequiredIndicatorVisible(true);
                password.setRequiredIndicatorVisible(true);

                // Set the layout as the root layout of the panel
                loginPanel.setContent(loginLayout);

                setCompositionRoot(loginPanel);

                // login button onclick event implementation
                login.addClickListener((ClickEvent event) -> {
                        String userNameVal = userName.getValue();
                        String passVal = password.getValue();
                        User loggedUser = userRepository.findByUserNameAndPassword(userNameVal, passVal);
                        System.out.println("-----" + loggedUser.getUserName() + loggedUser.getPassword() + "-----");
                        getUI().getNavigator().navigateTo(SwapiView.VIEW_NAME);
                });

        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
        }

}
