package com.zygimantus.apiwebas.vaadin.ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
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
