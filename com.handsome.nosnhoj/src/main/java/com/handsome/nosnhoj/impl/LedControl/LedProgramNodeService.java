package com.handsome.nosnhoj.impl.LedControl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class LedProgramNodeService implements SwingProgramNodeService<LedProgramNodeContribution, LedProgramNodeView>{

	@Override
	public String getId() {
		return "LedProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		return "LED Control";
	}

	@Override
	public LedProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new LedProgramNodeView(apiProvider);
	}

	@Override
	public LedProgramNodeContribution createNode(ProgramAPIProvider apiProvider, LedProgramNodeView view,
			DataModel model, CreationContext context) {
		return new LedProgramNodeContribution(apiProvider, view, model);
	}

}
