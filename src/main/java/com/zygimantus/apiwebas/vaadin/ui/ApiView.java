package com.zygimantus.apiwebas.vaadin.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.navigator.View;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.model.BaseCountry;
import com.zygimantus.apiwebas.vaadin.model.RegionalBloc;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;

/**
 *
 * @author Zygimantus
 */
public abstract class ApiView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	protected ApiwebasRepository apiwebasRepository;

	protected TabSheet tabs;

	protected boolean usePagination;
	protected int currentTotal;
	protected int page = 1;
	protected int limit;

	protected String searchToken;

	@PostConstruct
	public void init() {
		addComponent(new VerticalLayout(new Menu()));

		tabs = new TabSheet();

		initTabs();

		tabs.addSelectedTabChangeListener(e -> {
			Component tab = e.getTabSheet().getSelectedTab();
			if (tab instanceof ResourceTab) {
				ResourceTab rt = (ResourceTab) tab;
				rt.removeAllComponents();

				List<?> list = getResourceData(rt.getResource());

				HashMap config = rt.getResource().getConfig();

				if (config != null) {
					TextField tf = (TextField) config.get("textField");

					tf.setWidth("100%");

					tf.addValueChangeListener(l -> {
						searchToken = l.getValue();
						rt.getGrid().setItems(getResourceData(rt.getResource()));						
					});
					
					addComponent(tf);
				}

				rt.createGrid(list);

				if (rt.getResource().isUsePagination()) {
					page = 1;
					Pagination pagination = createPagination(currentTotal, page, limit);
					pagination.addPageChangeListener(l -> {

						List<?> listPaged = getResourceData(rt.getResource());

						rt.getGrid().setItems(listPaged);

						pagination.setTotalCount(currentTotal);

						page = l.page();
					});

					rt.addComponent(pagination);
				}

				save(list, rt.getResource());
			}
		});

		addComponent(tabs);
	}

	protected abstract void initTabs();

	protected abstract List<?> getResourceData(Resource resource);

	protected void save(List<?> list, Resource resource) {
		try {
			SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();

			list.forEach((t) -> {
				session.merge(t);
			});

			transaction.commit();

			Apiwebas apiwebas = new Apiwebas(resource, true);
			apiwebasRepository.save(apiwebas);
		} catch (Exception ex) {

		}
	}

	protected Pagination createPagination(long total, int page, int limit) {
		final PaginationResource paginationResource = PaginationResource.newBuilder().setTotal(total).setPage(page)
				.setLimit(limit).build();
		final Pagination pagination = new Pagination(paginationResource);
		// TODO make constant in SportDeerClient
		pagination.setItemsPerPage(30);
		return pagination;
	}

}
