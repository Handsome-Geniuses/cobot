package com.handsome.nosnhoj.impl.Paint.PaintToolbar;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.ur.urcap.api.contribution.toolbar.ToolbarConfiguration;
import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;

public class PaintToolbarService implements SwingToolbarService{
	
	@Override
	public Icon getIcon() {
		return new ImageIcon(getClass().getResource("/icons/paint.png"));
	}

	@Override
	public void configureContribution(ToolbarConfiguration configuration) {
		configuration.setToolbarHeight(235);
	}

	@Override
	public SwingToolbarContribution createToolbar(ToolbarContext context) {
		return new PaintToolbarContribution(context);
	}

}
