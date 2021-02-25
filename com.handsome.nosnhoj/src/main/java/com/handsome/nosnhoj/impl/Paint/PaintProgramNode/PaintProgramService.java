package com.handsome.nosnhoj.impl.Paint.PaintProgramNode;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class PaintProgramService implements SwingProgramNodeService<PaintProgramContribution, PaintProgramView>{

	@Override
	public String getId() {
		return "PaintProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Paint Move";
	}

	@Override
	public PaintProgramView createView(ViewAPIProvider apiProvider) {
		return new PaintProgramView(apiProvider);
	}

	@Override
	public PaintProgramContribution createNode(ProgramAPIProvider apiProvider, 
			PaintProgramView view, DataModel model, CreationContext context) {
		return new PaintProgramContribution(apiProvider, view, model);
	}
}
