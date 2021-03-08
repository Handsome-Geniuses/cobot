package com.handsome.nosnhoj.impl.Template.ProgramNode;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class TemplateProgramService implements SwingProgramNodeService<TemplateProgramContribution, TemplateProgramView>{

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "template node";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		// TODO Auto-generated method stub
		return "template";
	}

	@Override
	public TemplateProgramView createView(ViewAPIProvider apiProvider) {
		return new TemplateProgramView(apiProvider);
	}

	@Override
	public TemplateProgramContribution createNode(ProgramAPIProvider apiProvider, TemplateProgramView view,
			DataModel model, CreationContext context) {
		return new TemplateProgramContribution(apiProvider, view, model);
	}

}
