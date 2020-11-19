package com.handsome.nosnhoj.impl.Comms.ReadProgram;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class PortReadProgramView implements SwingProgramNodeView<PortReadProgramContribution>{
	
	public PortReadProgramView() {
	}
	@Override
	public void buildUI(JPanel panel, ContributionProvider<PortReadProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(CreateNodeInfo());
		
	}
	
	private Box CreateNodeInfo() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel("Whats up."));
		return box;
	}

}
