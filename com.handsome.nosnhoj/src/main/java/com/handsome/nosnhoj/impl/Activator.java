package com.handsome.nosnhoj.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.handsome.nosnhoj.impl.Comms.CommsToolBar.CommsToolbarService;
import com.handsome.nosnhoj.impl.Comms.Daemon.CommsDaemonService;
import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationService;
import com.handsome.nosnhoj.impl.Comms.PortSetupProgram.PortSetupProgramService;
import com.handsome.nosnhoj.impl.Comms.ReadProgram.PortReadProgramService;
import com.handsome.nosnhoj.impl.Comms.SendProgram.CommsProgramService;
import com.handsome.nosnhoj.impl.LockLED.InstallationNode.LockLEDInstallationService;
import com.handsome.nosnhoj.impl.LockLED.ProgramNode.LockLEDProgramService;
import com.handsome.nosnhoj.impl.Paint.PaintProgramNode.PaintProgramService;
import com.handsome.nosnhoj.impl.Paint.PaintToolbar.PaintToolbarService;
import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;


public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println(">>>>>Nosnhoj says whatsgud.");
		
		/*========================================================================================
         * Installation
         * ======================================================================================*/
		CommsDaemonService daemonService = new CommsDaemonService();
		CommsInstallationService installationNodeService = new CommsInstallationService(daemonService);
        context.registerService(SwingInstallationNodeService.class, installationNodeService, null);

        /*========================================================================================
         * Toolbar
         * ======================================================================================*/
        context.registerService(SwingToolbarService.class, new CommsToolbarService(), null);
        context.registerService(SwingToolbarService.class, new PaintToolbarService(), null);
        
        /*========================================================================================
         * Program
         * ======================================================================================*/
//		context.registerService(SwingProgramNodeService.class, new PortSetupProgramService(), null);
//		context.registerService(SwingProgramNodeService.class, new PortReadProgramService(), null);
		context.registerService(SwingProgramNodeService.class, new CommsProgramService(), null);
		context.registerService(SwingProgramNodeService.class, new PaintProgramService(), null);
        
		
		context.registerService(DaemonService.class, daemonService, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println(">>>>>Nosnhoj says latuh muh gatuh");
	}
}

