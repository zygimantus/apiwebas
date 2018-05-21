package com.zygimantus.apiwebas.vaadin.ui;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import com.zygimantus.apiwebas.vaadin.model.Api;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.sportdeerclient.SportDeer;
import com.zygimantus.sportdeerclient.SportDeerApi;
import com.zygimantus.sportdeerclient.SportDeerCountries;
import com.zygimantus.sportdeerclient.SportDeerFixtures;
import com.zygimantus.sportdeerclient.SportDeerLeagues;

import retrofit2.Response;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SportDeerView.VIEW_NAME)
public final class SportDeerView extends ApiView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "sportDeer";

	@Value("${sportDeerRefreshToken}")
	private String sportDeerRefreshToken;

	@Override
	public void init() {

		super.init();
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	protected void initTabs() {
		for (Resource resource : Api.SPORTDEER.getResources()) {

			ResourceTab st = new ResourceTab(resource);

			TabSheet.Tab tab = tabs.addTab(st);
			tab.setCaption(resource.name());
		}
	}

	@Override
	protected List<?> getResourceData(Resource resource) {

		SportDeerApi.init();
		SportDeer api = SportDeerApi.getApi();

		Response<com.zygimantus.sportdeerclient.SportDeerAccessToken> response;
		try {
			response = api.getAccessToken(sportDeerRefreshToken).execute();

			List<?> list = null;

			switch (resource) {
			case SPORTDEER_COUNTRIES:
				Response<SportDeerCountries> response1;
				response1 = api.getCountries(response.body().getNewAccessToken()).execute();
				list = response1.body().getDocs();
				currentTotal = response1.body().getPagination().getTotal();
				limit = response1.body().getPagination().getLimit();
				break;
			case SPORTDEER_FIXTURES:
				Response<SportDeerFixtures> response2;
				response2 = api.getFixtures(null, null, response.body().getNewAccessToken(), page).execute();
				list = response2.body().getDocs();
				currentTotal = response2.body().getPagination().getTotal();
				limit = response2.body().getPagination().getLimit();
				break;
			case SPORTDEER_LEAGUES:
				Response<SportDeerLeagues> response3;
				response3 = api.getLeagues(response.body().getNewAccessToken(), page).execute();
				list = response3.body().getDocs();
				currentTotal = response3.body().getPagination().getTotal();
				limit = response3.body().getPagination().getLimit();
				break;
			default:
				break;
			}
			
			save(list, resource);

			return list;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

}
