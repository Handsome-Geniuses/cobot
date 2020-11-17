package com.handsome.nosnhoj.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.handsome.nosnhoj.impl.Comms.Daemon.CommsDaemonService;
import com.handsome.nosnhoj.impl.Comms.Installation.CommsInstallationService;
import com.handsome.nosnhoj.impl.Comms.PortSetupProgram.PortSetupProgramService;
import com.handsome.nosnhoj.impl.Comms.SendProgram.CommsProgramService;
import com.handsome.nosnhoj.impl.LockLED.InstallationNode.LockLEDInstallationService;
import com.handsome.nosnhoj.impl.LockLED.ProgramNode.LockLEDProgramService;
import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;


public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println(">>>>>Nosnhoj says whatsgud.");
		
		//daemon stuff
		CommsDaemonService daemonService = new CommsDaemonService();
		CommsInstallationService installationNodeService = new CommsInstallationService(daemonService);

		//host led node
//		context.registerService(SwingProgramNodeService.class, new LedProgramNodeService(), null);
		//guest led node
		//installation before program just in case
		context.registerService(SwingInstallationNodeService.class, new LockLEDInstallationService(), null);
		context.registerService(SwingProgramNodeService.class, new LockLEDProgramService(), null);
		
		
		context.registerService(SwingInstallationNodeService.class, installationNodeService, null);
		context.registerService(SwingProgramNodeService.class, new PortSetupProgramService(), null);
		context.registerService(SwingProgramNodeService.class, new CommsProgramService(), null);
		context.registerService(DaemonService.class, daemonService, null);
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println(">>>>>Nosnhoj says latuh muh gatuh");
	}
}

