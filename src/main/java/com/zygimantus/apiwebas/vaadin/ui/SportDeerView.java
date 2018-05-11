package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.zygimantus.sportdeerclient.Doc;
import com.zygimantus.sportdeerclient.Doc_;
import com.zygimantus.sportdeerclient.SportDeer;
import com.zygimantus.sportdeerclient.SportDeerApi;
import com.zygimantus.sportdeerclient.SportDeerCountries;
import com.zygimantus.sportdeerclient.SportDeerFixtures;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Response;

/**
 *
 * @author Zygimantus
 */
@SpringView(name = SportDeerView.VIEW_NAME)
public final class SportDeerView extends ApiView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "sportDeer";

	private Grid<Doc_> grid;

	@Value("${sportDeerRefreshToken}")
	private String sportDeerRefreshToken;

	private int currentPage = 1;

	@Override
	public void init() {

		super.init();

		grid = new Grid<>(Doc_.class);

		addComponent(grid);

		grid.setWidth("100%");

	}

	@Override
	public void enter(ViewChangeEvent event) {

		final Pagination pagination = createPagination(1000, currentPage, 10);
		pagination.addPageChangeListener(e -> {

			pagination.setTotalCount(listData(e.page()));

			currentPage = e.page();
		});

		addComponent(pagination);

		listData(currentPage);

		// save(list, Resource.FOOTBALL_COUNTRIES);
	}

	private int listData(int page) {

		SportDeerApi.init();
		SportDeer api = SportDeerApi.getApi();

		Response<com.zygimantus.sportdeerclient.SportDeerAccessToken> response;
		try {
			response = api.getAccessToken(sportDeerRefreshToken).execute();

			Response<SportDeerFixtures> response1;
			response1 = api.getFixtures(null, null, response.body().getNewAccessToken(), page).execute();
			List list = response1.body().getDocs();

			grid.setItems(list);

			return response1.body().getPagination().getTotal();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return 0;
	}

	private Pagination createPagination(long total, int page, int limit) {
		final PaginationResource paginationResource = PaginationResource.newBuilder().setTotal(total).setPage(page)
				.setLimit(limit).build();
		final Pagination pagination = new Pagination(paginationResource);
		// TODO make constant in SportDeerClient
		pagination.setItemsPerPage(30);
		return pagination;
	}

}
