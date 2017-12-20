package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.annotations.Theme;
import org.springframework.beans.factory.annotation.Autowired;

import com.zygimantus.apiwebas.vaadin.repo.UserRepository;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.api.PokeApiConsumer;
import com.zygimantus.apiwebas.vaadin.api.TheMovieDBConsumer;
import java.util.List;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;

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

        @Autowired
        private TheMovieDBConsumer theMovieDBConsumer;
        @Autowired
        private PokeApiConsumer pokeApiConsumer;

        @Override
        protected void init(VaadinRequest request) {

                VerticalLayout mainLayout = new VerticalLayout();

                LoginView loginView = new LoginView(userRepository);

                mainLayout.addComponent(loginView);
                mainLayout.setSizeFull();
                mainLayout.setComponentAlignment(loginView, Alignment.MIDDLE_CENTER);

                setContent(mainLayout);

//                SwapiView swapiView = new SwapiView(swApiConsumer);
                MovieDBView movieDBView = new MovieDBView(theMovieDBConsumer);

                List<NamedApiResource> list = pokeApiConsumer.getPokemonList(0, 10);
                navigator.setErrorView(ErrorView.class);

                navigator.navigateTo("login");
        }

}
