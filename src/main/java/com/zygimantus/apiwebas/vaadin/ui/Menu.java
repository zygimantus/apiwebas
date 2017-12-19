package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

/**
 *
 * @author Zygimantus
 */
public final class Menu extends MenuBar {

        public Menu() {
                setWidth("100%");
                addItem("Home", (MenuItem selectedItem) -> {
                        UI.getCurrent().getNavigator().navigateTo(SwapiView.VIEW_NAME);
                });
                MenuItem mainItem = addItem("Apis", null);
                mainItem.addItem("Swapi", (MenuItem selectedItem) -> {
                        UI.getCurrent().getNavigator().navigateTo(SwapiView.VIEW_NAME);                        
                });
                mainItem.addItem("MovieDB", (MenuItem selectedItem) -> {
                        UI.getCurrent().getNavigator().navigateTo(MovieDBView.VIEW_NAME);                        
                });
                mainItem.addItem("Test", (MenuItem selectedItem) -> {
                });
                mainItem.addItem("Test", (MenuItem selectedItem) -> {
                });
        }
}
