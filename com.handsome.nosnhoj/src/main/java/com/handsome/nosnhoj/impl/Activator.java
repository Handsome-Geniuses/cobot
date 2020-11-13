package com.handsome.nosnhoj.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.handsome.nosnhoj.impl.Comms.Daemon.CommsDaemonService;
import com.handsome.nosnhoj.impl.Comms.Installation.CommsInstallationService;
import com.handsome.nosnhoj.impl.Comms.PortSetupProgram.PortSetupProgramService;
import com.handsome.nosnhoj.impl.Comms.SendProgram.CommsProgramService;
import com.handsome.nosnhoj.impl.HelloInstallation.HelloInstallationNodeService;
import com.handsome.nosnhoj.impl.HelloToolbar.HelloToolbarService;
import com.handsome.nosnhoj.impl.LedControl.LedProgramNodeService;
import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;


public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println(">>>>>Nosnhoj says whatsgud.");
//		HelloDaemonDaemonService daemonService = new HelloDaemonDaemonService();
//		HelloDaemonInstallationService installationService = new HelloDaemonInstallationService(daemonService);
//		
//		context.registerService(SwingInstallationNodeService.class, installationService, null);
//		context.registerService(DaemonService.class, daemonService, null);
		
		CommsDaemonService daemonService = new CommsDaemonService();
		CommsInstallationService installationNodeService = new CommsInstallationService(daemonService);
		
//		context.registerService(SwingToolbarService.class, new HelloToolbarService(), null);
//		context.registerService(SwingInstallationNodeService.class, new HelloInstallationNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new LedProgramNodeService(), null);
		
		context.registerService(SwingInstallationNodeService.class, installationNodeService, null);
		context.registerService(SwingProgramNodeService.class, new PortSetupProgramService(), null);
		context.registerService(SwingProgramNodeService.class, new CommsProgramService(), null);
//		context.registerService(SwingProgramNodeService.class, new MyDaemonProgramNodeService(), null);
		context.registerService(DaemonService.class, daemonService, null);
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println(">>>>>Nosnhoj says latuh muh gatuh");
	}
}

