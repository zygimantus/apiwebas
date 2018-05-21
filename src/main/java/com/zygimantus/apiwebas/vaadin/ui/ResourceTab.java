package com.zygimantus.apiwebas.vaadin.ui;

import java.util.List;

import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.zygimantus.apiwebas.vaadin.model.Resource;

/**
 * 
 * @author Zygimantus
 *
 */
public class ResourceTab extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private final Resource resource;
	private Grid<?> grid;

	public ResourceTab(Resource resource) {
		this.resource = resource;
	}

	public void createGrid(List list) {
		grid = new Grid<>(resource.getAClass());

		String[] colIds = resource.getColumnIds();
		if (colIds.length != 1) {
			grid.setColumns(colIds);
		}
		grid.setWidth("100%");
		grid.setItems(list);

		addComponent(grid);
	}

	public Resource getResource() {
		return resource;
	}

	public Grid getGrid() {
		return grid;
	}

}
