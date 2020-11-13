package com.handsome.nosnhoj.impl.HelloToolbar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;

public class HelloToolbarContribution implements SwingToolbarContribution{

	private final ToolbarContext context;
	
	public HelloToolbarContribution(ToolbarContext context) {
		this.context = context;
	}
	
	@Override
	public void buildUI(JPanel panel) {
		// TODO Auto-generated method stub
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
		panel.add(CreateTitle("Hello Toolbar"));
	}
	
	private Box CreateTitle(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel label = new JLabel(s);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 24));
		
		box.add(label);
		box.add(CreateSpace(0, 20));
		
		return box;
	}
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}

	@Override
	public void openView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
		
	}

}
