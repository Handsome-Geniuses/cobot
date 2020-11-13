package com.handsome.nosnhoj.impl.Comms.SendProgram;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class CommsProgramView implements SwingProgramNodeView<CommsProgramContribution>{
	private static final int gap_sm = 10;
	private static final int gap_md = 25;
	private static final int gap_lg = 50; 
	
	private final ViewAPIProvider apiProvider;
	
	private JTextField UserInput;
	private JButton SendButton;
	
	
	public CommsProgramView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<CommsProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(CreateNodeInfo());
		panel.add(CreateSpace(0, gap_md));
		panel.add(CreateInput(provider));
		panel.add(CreateSpace(0, gap_sm));
		panel.add(CreateSendButton(provider));
	}
	
	public void SetTextField(String in) {
		UserInput.setText(in);
	}
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}
	
	private Box CreateNodeInfo() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel("This node will send a message through port."));
		return box;
	}
	
	private Box CreateInput(final ContributionProvider<CommsProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		UserInput = new JTextField();
		UserInput.setFocusable(false);
		UserInput.setPreferredSize(new Dimension(230, 30));
		UserInput.setMaximumSize(UserInput.getPreferredSize());
		UserInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardTextInput = provider.get().GetInputTextField();
				keyboardTextInput.show(UserInput, provider.get().GetInputCallback());
			}
		});
		
		box.add(new JLabel("Message: "));
		box.add(CreateSpace(gap_sm, 0));
		box.add(UserInput);
		
		return box;
	}

	
	private Box CreateSendButton(final ContributionProvider<CommsProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		SendButton = new JButton("send");
		SendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				provider.get().OnSendClick();
			}
		});
		
		box.add(SendButton);
		return box;
	}
	
}
