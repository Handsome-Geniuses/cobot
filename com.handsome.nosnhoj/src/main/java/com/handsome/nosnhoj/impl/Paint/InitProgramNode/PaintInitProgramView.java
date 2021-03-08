package com.handsome.nosnhoj.impl.Paint.InitProgramNode;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class PaintInitProgramView implements SwingProgramNodeView<PaintInitProgramContribution>{
	
	private JCheckBox activate; 

	private final ViewAPIProvider apiProvider;
	public PaintInitProgramView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	/*========================================================================================
     * Constructor and Build
     * ======================================================================================*/
	@Override
	public void buildUI(JPanel panel, ContributionProvider<PaintInitProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
//		panel.add(CreateTitle("Paint Activate"));
		panel.add(CreateMessage("Activates paints gun. Centers gun if hasn't been centered yet."));
		
		panel.add(CreateActivateBox(provider));
		
	}
	
	private Box CreateTitle(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(s);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 24));
		label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		box.add(label);
		return box;
	}
	
	/*========================================================================================
     * Get and sets
     * ======================================================================================*/
	public void SetCheck(Boolean b) {
		activate.setSelected(b);
	}
	public Boolean GetCheck() {
		return activate.isSelected();
	}
	
	/*========================================================================================
     * UI Components
     * ======================================================================================*/
	private Box CreateMessage(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(s);
		label.setFont(label.getFont().deriveFont(Font.PLAIN, 16));
		label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		box.add(label);
		return box;
	}
	
	private Box CreateActivateBox(final ContributionProvider<PaintInitProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		activate = new JCheckBox("Activate Only", false);
		activate.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		activate.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				provider.get().OnCheck(activate.isSelected());
			}
		});
		
		box.add(activate);
		return box;
	}
	

}
