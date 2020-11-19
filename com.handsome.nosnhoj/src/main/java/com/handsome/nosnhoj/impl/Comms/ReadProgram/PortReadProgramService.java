package com.handsome.nosnhoj.impl.Comms.ReadProgram;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class PortReadProgramService implements SwingProgramNodeService<PortReadProgramContribution, PortReadProgramView>{

	@Override
	public String getId() {
		return "PortReadProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle(Locale locale) {
		return "Port Read";
	}

	@Override
	public PortReadProgramView createView(ViewAPIProvider apiProvider) {
		return new PortReadProgramView();
	}

	@Override
	public PortReadProgramContribution createNode(ProgramAPIProvider apiProvider, PortReadProgramView view,
			DataModel model, CreationContext context) {
		return new PortReadProgramContribution();
	}
	

}
