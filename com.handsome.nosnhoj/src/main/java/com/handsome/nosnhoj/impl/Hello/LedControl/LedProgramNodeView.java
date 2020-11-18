package com.handsome.nosnhoj.impl.Hello.LedControl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class LedProgramNodeView implements SwingProgramNodeView<LedProgramNodeContribution>{

	private final ViewAPIProvider apiProvider;
	private JCheckBox FlashBox = new JCheckBox(" ");
	private int pins_Zone1[] = {2, 1, 3};
	private JCheckBox Zone1[] = {new JCheckBox("Red"), new JCheckBox("Green"), new JCheckBox("Blue")};
	private boolean[] status = {false, false, false};

	public LedProgramNodeView(ViewAPIProvider apiProvider) {
        this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<LedProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));	//organizes components on the y axis
		
		panel.add(CreateFlashBox("Flash: ", FlashBox, 0, provider));
		panel.add(createSpacer(0, 20));
		panel.add(CreateCheckBoxes("Zone 1:", Zone1, pins_Zone1, provider));
	}

	public void SetFlash(boolean state) {
		FlashBox.setSelected(state);
	}
	
	private Box CreateFlashBox(String desc, final JCheckBox check, final int pin,
			final ContributionProvider<LedProgramNodeContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(desc);
		
		check.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent item) { 
				if(item.getStateChange()==ItemEvent.SELECTED) {
					check.setForeground(new Color(155, 155, 155));
					provider.get().FlashChange(true);
				}
				else {
					check.setForeground(new Color(0, 0, 0));
					provider.get().FlashChange(false);
				}
			}
		});
		
		box.add(label);
		box.add(createSpacer(60, 0));
		box.add(check);
		
		return box;
	}
	
	public void SetChecks(boolean[] states) {
		for (int i = 0; i < states.length; i++) {
			Zone1[i].setSelected(states[i]);
			status[i]=states[i];
		}
	}
	
	public int[] GetPins() {
		return pins_Zone1;
	}
	
	private Box CreateCheckBoxes(String desc, final JCheckBox[] check, final int[] pins,
			final ContributionProvider<LedProgramNodeContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(desc);
		
		for (int i = 0; i < check.length; i++) {
			final int j = i;
			check[j].addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent item) { 
					if(item.getStateChange()==ItemEvent.SELECTED) {
						check[j].setForeground(new Color(155, 155, 155));
						status[j] = true; 
					}
					else {
						check[j].setForeground(new Color(0, 0, 0));
						status[j] = false; 
					}
					provider.get().CheckChange(status);
				}
			});
		}
		
		
		box.add(label);
		box.add(createSpacer(60, 0));
		for (int i = 0; i < check.length; i++) {
			box.add(check[i]);
			box.add(createSpacer(40, 0));
		}
		return box;
	}
	
	
	private Component createSpacer(int width, int height) {
		return Box.createRigidArea(new Dimension(width, height));
	}

}
