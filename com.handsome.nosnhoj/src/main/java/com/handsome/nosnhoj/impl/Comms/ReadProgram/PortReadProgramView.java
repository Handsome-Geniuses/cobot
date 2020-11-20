package com.handsome.nosnhoj.impl.Comms.ReadProgram;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class PortReadProgramView implements SwingProgramNodeView<PortReadProgramContribution>{
	
	public PortReadProgramView() {
	}
	@Override
	public void buildUI(JPanel panel, ContributionProvider<PortReadProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(CreateLabel("Place this node in a thread."));
		panel.add(CreateLabel("Messages will be read into get_read_string() function."));
		panel.add(CreateLabel("Children node only ran after a message is received."));
	}
	
	private Box CreateLabel(String desc) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel(desc));
		return box;
	}

}
