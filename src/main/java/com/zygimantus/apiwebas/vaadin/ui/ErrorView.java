package com.zygimantus.apiwebas.vaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
@SpringView
public class ErrorView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}

	@PostConstruct
	void init() {
		addComponent(new Label("Hello, this is the 'error view' loaded if no view is matched based on URL."));
	}
}
