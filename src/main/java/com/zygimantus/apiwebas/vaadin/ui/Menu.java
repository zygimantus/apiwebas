package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.zygimantus.apiwebas.vaadin.model.Api;
import com.zygimantus.apiwebas.vaadin.util.Db2Sql;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 *
 * @author Zygimantus
 */
public final class Menu extends MenuBar {

	private static final long serialVersionUID = 1L;

	public Menu() {
        setWidth("100%");
        addItem("Home", (MenuItem selectedItem) -> {
            UI.getCurrent().getNavigator().navigateTo(SwapiView.VIEW_NAME);
        });
        //
        MenuItem mainItem = addItem("Apis", null);

        for (Api api : Api.values()) {
            mainItem.addItem(api.getName(), (MenuItem selectedItem) -> {
                UI.getCurrent().getNavigator().navigateTo(api.getViewName());
            });
        }
        //
        MenuItem toolsItem = addItem("Tools", null);
        toolsItem.addItem("Export to CSV", (MenuItem selectedItem) -> {
            UI.getCurrent().getNavigator().navigateTo(ExportView.VIEW_NAME);
        });
        toolsItem.addItem("Create MySQL Dump", (MenuItem selectedItem) -> {
            ClassLoader classLoader = Menu.class.getClassLoader();
            File file = new File(classLoader.getResource("db2sql.properties").getFile());
            String sFile = file.getAbsolutePath();

            Properties props = new Properties();
            try {
                props.load(new FileInputStream(sFile));
                try (PrintWriter out = new PrintWriter("dump.sql")) {
                    out.println(Db2Sql.dumpDB(props));
                }

                FileResource res = new FileResource(new File(System.getProperty("user.dir") + "/dump.sql"));
                Page.getCurrent().open(res, null, false);
            } catch (IOException e) {
                System.err.println("Unable to open property file: " + sFile + " exception: " + e);
            }
        });
    }
}
