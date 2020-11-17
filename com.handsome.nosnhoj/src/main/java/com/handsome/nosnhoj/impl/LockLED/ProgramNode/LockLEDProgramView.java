package com.handsome.nosnhoj.impl.LockLED.ProgramNode;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;


public class LockLEDProgramView implements SwingProgramNodeView<LockLEDProgramContribution>{
	
	//static defines
	private static final int gap_sm = 10;
	private static final int gap_md = 25;
	private static final int gap_lg = 50; 
	
	//ui data elements
	private JCheckBox ZoneCheckBoxes[] = {	new JCheckBox("1", false), 
											new JCheckBox("2", false), 
											new JCheckBox("3", false)};
	private JSlider RGBSliders[] = {		new JSlider(JSlider.HORIZONTAL,0, 9, 0), 
											new JSlider(JSlider.HORIZONTAL,0, 9, 0), 
											new JSlider(JSlider.HORIZONTAL,0, 9, 0)};
	
	private JCheckBox FlashBox = new JCheckBox("Flash", false);
	private JSlider DurationSliders[] = {	new JSlider(JSlider.HORIZONTAL,0, 2000, 0), 
											new JSlider(JSlider.HORIZONTAL,0, 2000, 0)};
	
	static String colors[] = {"Red   ", "Green", "Blue   "};
	private JLabel value[] = {new JLabel("0%"), new JLabel("0%"), new JLabel("0%")};
	private String durstring[] = {"On", "Off"};
	private JLabel duration[] = {new JLabel("0s"), new JLabel("0s")};
	
	//constructor members
	private final ViewAPIProvider apiProvider;
	
	public LockLEDProgramView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<LockLEDProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(CreateInfo("Select zone and brightness values for red, green, and blue."));
		panel.add(CreateSpace(0, gap_sm));
		panel.add(CreateZoneCheckBoxes(provider));
		panel.add(CreateSpace(0, gap_sm));
		
		for (int i = 0; i < RGBSliders.length; i++) {
			panel.add(CreateRGBSlider(provider, colors[i], RGBSliders[i], value[i]));
		}
		
		panel.add(CreateSpace(0, gap_lg));
		panel.add(CreateInfo("Enable flash and select on and off duration if desired."));
		panel.add(CreateFlashBox(provider));
		for (int i = 0; i < DurationSliders.length; i++) {
			panel.add(BoxCreateDurationSliders(provider, durstring[i] ,DurationSliders[i], duration[i]));
		}
		
	}
	
	public void SetZone(int z) {
		for (int i = 0; i < ZoneCheckBoxes.length; i++) {
			if(i==z) {
				ZoneCheckBoxes[i].setSelected(true);
				ZoneCheckBoxes[i].setEnabled(false);
			}
			else {
				ZoneCheckBoxes[i].setSelected(false);
				ZoneCheckBoxes[i].setEnabled(true);
			}
		}
	}
	public void SetFlashBox(boolean f) {
		FlashBox.setSelected(f);
		DurationSliders[0].setEnabled(f);
		DurationSliders[1].setEnabled(f);
	}
	public void SetRGBSliders(int colors[]) {
		for (int i = 0; i < colors.length; i++) {
			RGBSliders[i].setValue(colors[i]);
			value[i].setText(Integer.toString(colors[i]<1?0:colors[i]*10)+"%");
		}
	}
	public void SetDurationSliders(int dur[]) {
		for (int i = 0; i < DurationSliders.length; i++) {
			DurationSliders[i].setValue(dur[i]);
			duration[i].setText(new DecimalFormat("#.###").format((float)(dur[i])/1000)+"s");
		}
	}
	
	public boolean GetFlashStatus() {
		return FlashBox.isSelected();
	}
	public int[] GetSliderRGB(){
		int temp[] = {RGBSliders[0].getValue(), RGBSliders[1].getValue(), RGBSliders[2].getValue()};
		return temp;
	}
	public int[] GetSliderDurations() {
		int temp[] = {DurationSliders[0].getValue(), DurationSliders[1].getValue()};
		return temp;
	}
	
	public Boolean isZoneSelected() {
		return (ZoneCheckBoxes[0].isSelected() || ZoneCheckBoxes[1].isSelected() || ZoneCheckBoxes[2].isSelected());
	}
	
	private Box CreateInfo(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel(s));
		return box;
	}
	
	private Box CreateZoneCheckBoxes(final ContributionProvider<LockLEDProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		for (int i = 0; i < ZoneCheckBoxes.length; i++) {
			final int j = i;
			ZoneCheckBoxes[j].addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED) {
						ZoneCheckBoxes[j].setEnabled(false);
						for (int k = 0; k < ZoneCheckBoxes.length; k++) {
							if(j==k) continue;
							ZoneCheckBoxes[k].setEnabled(true);
							ZoneCheckBoxes[k].setSelected(false);
							provider.get().OnZoneSelect(j);
						}
					}
					//disabling uncheck option. need to have AT LEAST 1 selected to define
				}
			});
		}
		
		box.add(new JLabel("Zone Select: "));
		box.add(CreateSpace(60, 0));
		for (int i = 0; i < ZoneCheckBoxes.length; i++) {
			box.add(ZoneCheckBoxes[i]);
			box.add(CreateSpace(30, 0));
		}
		return box;	
	}
	
	private Box CreateFlashBox(final ContributionProvider<LockLEDProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		FlashBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					DurationSliders[0].setEnabled(true);
					DurationSliders[1].setEnabled(true);
				}
				else {
					DurationSliders[0].setEnabled(false);
					DurationSliders[1].setEnabled(false);
				}
			}
		});
		box.add(FlashBox);
		return box;
	}
	
	private Box CreateRGBSlider(final ContributionProvider<LockLEDProgramContribution> provider, 
			String s, final JSlider slider, final JLabel val) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		slider.setPreferredSize(new Dimension(300, 30));
		slider.setMaximumSize(slider.getPreferredSize());
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int v = slider.getValue();
				v = (v<1)?(0):(10*(v));
				val.setText(Integer.toString(v)+"%");
//				provider.get().OnColorChange(Intvalue[0].getText(), RGBSliders[1].getValue(), RGBSliders[2].getValue());
			}
		});
		
		box.add(new JLabel(s));
		box.add(CreateSpace(gap_sm, 0));
		box.add(slider);
		box.add(val);
		return box;
	}
	
	private Box BoxCreateDurationSliders(final ContributionProvider<LockLEDProgramContribution> provider,
			String s, final JSlider slider, final JLabel val) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		slider.setPreferredSize(new Dimension(300, 30));
		slider.setMaximumSize(slider.getPreferredSize());
		
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				float v = slider.getValue();
				v = (float) (v/1000);
				val.setText(new DecimalFormat("#.###").format(v)+"s");
			}
		});
		
		box.add(new JLabel(s));
		box.add(CreateSpace(gap_sm, 0));
		box.add(slider);
		box.add(val);
		return box;
	}
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}

}
