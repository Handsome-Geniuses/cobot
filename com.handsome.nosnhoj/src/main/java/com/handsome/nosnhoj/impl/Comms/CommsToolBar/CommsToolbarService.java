package com.handsome.nosnhoj.impl.Comms.CommsToolBar;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.ur.urcap.api.contribution.toolbar.ToolbarConfiguration;
import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;

public class CommsToolbarService implements SwingToolbarService {

	@Override
	public Icon getIcon() {
		return new ImageIcon(getClass().getResource("/icons/comms.png"));
	}

	@Override
	public void configureContribution(ToolbarConfiguration configuration) {
		configuration.setToolbarHeight(450);
	}

	@Override
	public SwingToolbarContribution createToolbar(ToolbarContext context) {
		return new CommsToolbarContribution(context);
	}

}
