package com.handsome.nosnhoj.impl.Hello.HelloInstallation;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

public class HelloInstallationNodeView implements SwingInstallationNodeView<HelloInstallationNodeContribution>{

	private final ViewAPIProvider apiProvider;
	
	public HelloInstallationNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}	
	
	@Override
	public void buildUI(JPanel panel, HelloInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(test_box());
		
	}
	
	private Box test_box() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel("installation view hello");
		
		box.add(label);
		
		return box;
	}

}
