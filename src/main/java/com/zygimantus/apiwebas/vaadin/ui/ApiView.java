package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Apiwebas;
import com.zygimantus.apiwebas.vaadin.model.Resource;
import com.zygimantus.apiwebas.vaadin.repo.ApiwebasRepository;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

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

	@PostConstruct
	public void init() {
		addComponent(new VerticalLayout(new Menu()));

		tabs = new TabSheet();

		initTabs();

		tabs.addSelectedTabChangeListener(e -> {
			Component tab = e.getTabSheet().getSelectedTab();
			if (tab instanceof ResourceTab) {
				ResourceTab st = (ResourceTab) tab;

				List<?> list = getResourceData(st.getResource());

				st.createGridAndSave(list);

				save(list, st.getResource());
			}
		});

		addComponent(tabs);
	}

	protected abstract void initTabs();

	protected abstract List<?> getResourceData(Resource resource);

	protected void save(List list, Resource resource) {
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		list.forEach((t) -> {
			session.merge(t);
		});

		transaction.commit();

		Apiwebas apiwebas = new Apiwebas(resource, true);
		apiwebasRepository.save(apiwebas);
	}

}
