package com.handsome.nosnhoj.impl.Paint.PaintToolbar;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

public class PaintToolbarContribution implements SwingToolbarContribution{

	private final ToolbarContext context;

    private JButton btn_up;
    private JButton btn_down;
    private JTextField degs;
    private KeyboardInputFactory numInput;
	
	public PaintToolbarContribution(ToolbarContext context) {
		this.context = context;
		numInput = context.getAPIProvider().getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
	}
	
	@Override
	public void buildUI(JPanel panel) {
		// TODO Auto-generated method stub
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
		panel.add(CreateTitle("Paint Control"));
		panel.add(CreateSpace(0, 10));
		panel.add(CreateDegreeControl());
	}
	
	private Box CreateTitle(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel label = new JLabel(s);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 24));
		label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		box.add(label);
		
		return box;
	}

	//pops up the numpad ui
	private KeyboardNumberInput<Double> GetKeyInput(){
		KeyboardNumberInput<Double> in = numInput.createDoubleKeypadInput();
		in.setInitialValue((double) 0);
		return in;
	}
	
	//handles numpad input
	private KeyboardInputCallback<Double> GetInputCallback(){
		return new KeyboardInputCallback<Double>() {

			@Override
			public void onOk(Double value) {
				if (value >= 135) value = (double) 135;
				else if (value <= -135) value = (double) -135;
				degs.setText(Double.toString(value));
			}
		};
	}
    private Box CreateDegreeControl(){
    	Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);

		btn_up = new JButton(new ImageIcon(getClass().getResource("/icons/plus50x50.png")));
        btn_up.setFocusable(false);
        btn_up.setPreferredSize(new Dimension(50, 50));
        btn_up.setMaximumSize(btn_up.getPreferredSize());
       
        btn_up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Double curr = Double.parseDouble(degs.getText())+5;
				if (curr>=135) curr = (double) 135;
				degs.setText(Double.toString(curr));
			}
		});

        btn_down = new JButton(new ImageIcon(getClass().getResource("/icons/minus50x50.png")));
        btn_down.setFocusable(false);
        btn_down.setPreferredSize(new Dimension(50, 50));
        btn_down.setMaximumSize(btn_down.getPreferredSize());
        
        btn_down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Double curr = Double.parseDouble(degs.getText())-5;
				if (curr<=-135) curr = (double) -135;
				degs.setText(Double.toString(curr));
			}
		});

        degs = new JTextField();
        degs.setFocusable(false);
        degs.setPreferredSize(new Dimension(100, 30));
        degs.setMaximumSize(degs.getPreferredSize());
        degs.setHorizontalAlignment(SwingConstants.CENTER);
        degs.setText("0");
        
        degs.addMouseListener(new MouseAdapter() {
        	@Override
			public void mousePressed(MouseEvent e) {
        		KeyboardNumberInput<Double> in = GetKeyInput();
        		in.show(degs, GetInputCallback());
        	}
		});

        box.add(btn_down);
        box.add(CreateSpace(3,0));
        box.add(degs);
        box.add(CreateSpace(2,0));
        box.add(btn_up);
        
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
