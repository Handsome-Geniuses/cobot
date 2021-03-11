package com.handsome.nosnhoj.impl.Comms.DaemonInstallation;

import com.handsome.nosnhoj.impl.Comms.Daemon.CommsDaemonService;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class CommsInstallationService implements SwingInstallationNodeService<CommsInstallationContribution, CommsInstallationView> {

	private final CommsDaemonService daemonService;

	public CommsInstallationService(CommsDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public CommsInstallationView createView(ViewAPIProvider apiProvider) {
		return new CommsInstallationView(apiProvider);
	}

	@Override
	public CommsInstallationContribution createInstallationNode(InstallationAPIProvider apiProvider, CommsInstallationView view, DataModel model, CreationContext context) {
		return new CommsInstallationContribution(apiProvider, view, model, daemonService, context);
	}

}
