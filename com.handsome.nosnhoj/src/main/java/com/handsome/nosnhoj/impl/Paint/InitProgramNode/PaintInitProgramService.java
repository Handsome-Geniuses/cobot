package com.handsome.nosnhoj.impl.Paint.InitProgramNode;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class PaintInitProgramService implements SwingProgramNodeService<PaintInitProgramContribution, PaintInitProgramView>{

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "paint init node";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		// TODO Auto-generated method stub
		return "Paint Activate";
	}

	@Override
	public PaintInitProgramView createView(ViewAPIProvider apiProvider) {
		return new PaintInitProgramView(apiProvider);
	}

	@Override
	public PaintInitProgramContribution createNode(ProgramAPIProvider apiProvider, PaintInitProgramView view,
			DataModel model, CreationContext context) {
		return new PaintInitProgramContribution(apiProvider, view, model);
	}

}
