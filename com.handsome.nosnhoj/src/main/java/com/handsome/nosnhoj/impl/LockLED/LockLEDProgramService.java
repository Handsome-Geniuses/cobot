package com.handsome.nosnhoj.impl.LockLED;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class LockLEDProgramService implements SwingProgramNodeService<LockLEDProgramContribution, LockLEDProgramView>{

	@Override
	public String getId() {
		return "LockLEDProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);	
	}

	@Override
	public String getTitle(Locale locale) {
		return "LED";
	}

	@Override
	public LockLEDProgramView createView(ViewAPIProvider apiProvider) {
		return new LockLEDProgramView(apiProvider);
	}

	@Override
	public LockLEDProgramContribution createNode(ProgramAPIProvider apiProvider, LockLEDProgramView view,
			DataModel model, CreationContext context) {
		return new LockLEDProgramContribution(apiProvider, view, model);
	}

}
