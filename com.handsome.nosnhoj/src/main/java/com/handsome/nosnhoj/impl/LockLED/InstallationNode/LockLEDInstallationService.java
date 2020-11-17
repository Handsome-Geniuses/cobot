package com.handsome.nosnhoj.impl.LockLED.InstallationNode;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class LockLEDInstallationService implements SwingInstallationNodeService<LockLEDInstallationContribution, LockLEDInstallationView>{

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Lock LED Installation";
	}

	@Override
	public LockLEDInstallationView createView(ViewAPIProvider apiProvider) {
		return new LockLEDInstallationView(apiProvider);
	}

	@Override
	public LockLEDInstallationContribution createInstallationNode(InstallationAPIProvider apiProvider,
			LockLEDInstallationView view, DataModel model, CreationContext context) {
		return new LockLEDInstallationContribution(apiProvider, view, model);
	}

}
