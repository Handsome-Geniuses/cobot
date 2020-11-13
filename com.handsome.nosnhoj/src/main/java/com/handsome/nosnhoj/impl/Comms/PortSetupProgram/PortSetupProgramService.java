package com.handsome.nosnhoj.impl.Comms.PortSetupProgram;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class PortSetupProgramService implements SwingProgramNodeService<PortSetupProgramContribution, PortSetupProgramView>{

	@Override
	public String getId() {
		return "PortSetupProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
		
	}

	@Override
	public String getTitle(Locale locale) {
		return "Port Setup";
	}

	@Override
	public PortSetupProgramView createView(ViewAPIProvider apiProvider) {
		return new PortSetupProgramView(apiProvider);
	}

	@Override
	public PortSetupProgramContribution createNode(ProgramAPIProvider apiProvider, 
			PortSetupProgramView view, DataModel model, CreationContext context) {
		return new PortSetupProgramContribution(apiProvider, view, model);
	}

}
