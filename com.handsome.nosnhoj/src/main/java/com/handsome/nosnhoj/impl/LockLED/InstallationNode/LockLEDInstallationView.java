package com.handsome.nosnhoj.impl.LockLED.InstallationNode;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

public class LockLEDInstallationView implements SwingInstallationNodeView<LockLEDInstallationContribution>{

	private final ViewAPIProvider apiProvider;
	
	public LockLEDInstallationView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, LockLEDInstallationContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(CreateInfo("Installation for functions, variables, and threads."));
			
	}
	
	private Box CreateInfo(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel(s));
		return box;
	}
}
