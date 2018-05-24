package com.zygimantus.apiwebas.vaadin.ui;

import java.io.IOException;
import java.util.List;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import com.zygimantus.apiwebas.vaadin.model.Api;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.thegamesdbclient.TheGamesDB;
import com.zygimantus.thegamesdbclient.TheGamesDBApi;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = TheGamesDBView.VIEW_NAME)
public final class TheGamesDBView extends ApiView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "theGamesDB";

	@Override
	public void init() {

		super.init();
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	protected void initTabs() {
		for (Resource resource : Api.GAMESDB.getResources()) {

			ResourceTab st = new ResourceTab(resource);

			TabSheet.Tab tab = tabs.addTab(st);
			tab.setCaption(resource.name());
		}
	}

	@Override
	protected List<?> getResourceData(Resource resource) {

		TheGamesDBApi.init();
		TheGamesDB api = TheGamesDBApi.getApi();

		List<?> list = null;

		try {
			switch (resource) {
			case GAMESDB_GAMESLIST:
				list = api.getGamesList(searchToken, null, null).execute().body().getGamesListGames();
				break;
			case GAMESDB_PLATFROMSLIST:
				list = api.getPlatformsList().execute().body().getPlatforms().getPlatformListPlatforms();
				break;
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// save(list, resource);

		return list;

	}

}
