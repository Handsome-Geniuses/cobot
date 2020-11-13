package com.handsome.nosnhoj.impl.HelloInstallation;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class HelloInstallationNodeService implements SwingInstallationNodeService<HelloInstallationNodeContribution, HelloInstallationNodeView>{

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Test?";
	}

	@Override
	public HelloInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new HelloInstallationNodeView(apiProvider);
	}

	@Override
	public HelloInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider,
			HelloInstallationNodeView view, DataModel model, CreationContext context) {
		return new HelloInstallationNodeContribution(apiProvider, view, model);
	}

}
