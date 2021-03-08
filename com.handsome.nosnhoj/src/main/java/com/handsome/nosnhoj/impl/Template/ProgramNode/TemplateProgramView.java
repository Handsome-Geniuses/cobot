package com.handsome.nosnhoj.impl.Template.ProgramNode;

import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class TemplateProgramView implements SwingProgramNodeView<TemplateProgramContribution>{

	private final ViewAPIProvider apiProvider;
	public TemplateProgramView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<TemplateProgramContribution> provider) {
		// TODO Auto-generated method stub
		
	}

}
