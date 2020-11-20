package com.handsome.nosnhoj.impl.Comms.SendProgram;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class CommsProgramService implements SwingProgramNodeService<CommsProgramContribution, CommsProgramView>{

	@Override
	public String getId() {
		return "CommsProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
		
	}

	@Override
	public String getTitle(Locale locale) {
		return "Port Send";
	}

	@Override
	public CommsProgramView createView(ViewAPIProvider apiProvider) {
		return new CommsProgramView(apiProvider);
	}

	@Override
	public CommsProgramContribution createNode(ProgramAPIProvider apiProvider, 
			CommsProgramView view, DataModel model, CreationContext context) {
		return new CommsProgramContribution(apiProvider, view, model);
	}

}
