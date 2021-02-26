package com.handsome.nosnhoj.impl.Paint.PaintProgramNode;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.handsome.nosnhoj.impl.Comms.Daemon.CommsXmlRpc;
import com.handsome.nosnhoj.impl.Paint.paintconfig;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

public class PaintProgramView implements SwingProgramNodeView<PaintProgramContribution> {

	private final ViewAPIProvider apiProvider;
	
	private JButton btn_up;
	private JButton btn_down;
	private JTextField degs;
	private JCheckBox halt;
	
	private JSlider speed_slider;
	private JLabel  speed_label;
	
	
	
//	private final CommsXmlRpc xml;	//goes to contribution i think
//	private KeyboardInputFactory numInput;	//goes to contribution too i think
	
	/*========================================================================================
     * Constructor and Build
     * ======================================================================================*/
	public PaintProgramView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<PaintProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
//		
		panel.add(CreateTitle("Angle"));
		panel.add(CreateSpace(0, 10));
		
		panel.add(CreateDegreeControl(provider));
		panel.add(CreateSpace(0, 25));
		panel.add(CreateSpeedSlider());
		panel.add(CreateSpace(0, 25));
		panel.add(CreateCheckBox("complete before next action"));
		
	}
	
	/*========================================================================================
     * Get and sets
     * ======================================================================================*/
	public String GetDegrees() {
		return degs.getText();
	}
	public void SetDegrees(String d) {
		degs.setText(d);
	}
	public Integer GetSpeed() {
		return speed_slider.getValue();
	}
	public void SetSpeed(String percent) {
		speed_slider.setValue(Integer.parseInt(percent));
		speed_label.setText(percent+"%");
	}
	
	/*========================================================================================
     * UI Components
     * ======================================================================================*/
	private Box CreateTitle(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel label = new JLabel(s);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 24));
		label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		box.add(label);
		
		return box;
	}
	
	private Box CreateCheckBox(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		halt = new JCheckBox(s, false);
		box.add(halt);
		return box;
	}
	
	private Box CreateDegreeControl(final ContributionProvider<PaintProgramContribution> provider){
    	Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);

		btn_up = new JButton(new ImageIcon(getClass().getResource("/icons/plus50x50.png")));
        btn_up.setFocusable(false);
        btn_up.setPreferredSize(new Dimension(50, 50));
        btn_up.setMaximumSize(btn_up.getPreferredSize());
       
        btn_up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer curr = Integer.parseInt(degs.getText()) + paintconfig.inc_btn;
				if(curr>=paintconfig.max_ang) curr = (Integer) paintconfig.max_ang;
				degs.setText(Integer.toString(curr));
				//TODO update to datamodel
			}
		});

        btn_down = new JButton(new ImageIcon(getClass().getResource("/icons/minus50x50.png")));
        btn_down.setFocusable(false);
        btn_down.setPreferredSize(new Dimension(50, 50));
        btn_down.setMaximumSize(btn_down.getPreferredSize());
        
        btn_down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer curr = Integer.parseInt(degs.getText()) - paintconfig.inc_btn;
				if(curr>=paintconfig.max_ang) curr = (Integer) paintconfig.max_ang;
				degs.setText(Integer.toString(curr));
				//TODO update to datamodel
			}
		});

        degs = new JTextField();
        degs.setFocusable(false);
        degs.setPreferredSize(new Dimension(100, 30));
        degs.setMaximumSize(degs.getPreferredSize());
        degs.setHorizontalAlignment(SwingConstants.CENTER);
        degs.setText("0");	//always default to 0.
        
        degs.addMouseListener(new MouseAdapter() {
        	@Override
			public void mousePressed(MouseEvent e) {
        		KeyboardNumberInput<Integer> in = provider.get().GetKeyInput();
        		in.show(degs, provider.get().GetInputCallback());
        	}
		});

        box.add(btn_down);
        box.add(CreateSpace(3,0));
        box.add(degs);
        box.add(CreateSpace(2,0));
        box.add(btn_up);
        
        return box;
    }
	
    private Box CreateSpeedSlider() {
    	Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		speed_label = new JLabel("100%");
		
		speed_slider = new JSlider(JSlider.HORIZONTAL, 10, 100, 100);	//10-100%, default 100
		speed_slider.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		speed_slider.setPreferredSize(new Dimension(300, 30));
		speed_slider.setMaximumSize(speed_slider.getPreferredSize());
		
		speed_slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int v = speed_slider.getValue(); //should be from 10 to 100
				speed_label.setText(Integer.toString(v)+"%");
			}
		});
		
		box.add(new JLabel("Speed: "));
		box.add(speed_slider);
		box.add(speed_label);
		
		return box;	
    }
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}

}
