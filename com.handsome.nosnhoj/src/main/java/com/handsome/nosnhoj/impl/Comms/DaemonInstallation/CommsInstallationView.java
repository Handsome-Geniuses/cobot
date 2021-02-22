package com.handsome.nosnhoj.impl.Comms.DaemonInstallation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CommsInstallationView implements SwingInstallationNodeView<CommsInstallationContribution> {

	private final ViewAPIProvider apiProvider;
	
	private JTextField popupInputField;
	private JButton startButton;
	private JButton stopButton;
	private JLabel statusLabel;
	
	private JButton sendButton;

	public CommsInstallationView(ViewAPIProvider apiProvider) {
		this.apiProvider=apiProvider;
	}

	@Override
	public void buildUI(JPanel panel, CommsInstallationContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(CreateStatus());
		panel.add(CreateSpace(0, 10));
		panel.add(CreateStartStopButtons(contribution));
		
		panel.add(CreateSpace(0, 50));
		
		panel.add(CreateInput(contribution));
		panel.add(CreateSpace(0, 10));
		panel.add(CreateSendButton(contribution));
	}

	private Box CreateInput(final CommsInstallationContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		

		popupInputField = new JTextField();
		popupInputField.setFocusable(false);
		popupInputField.setPreferredSize(new Dimension(230, 30));
		popupInputField.setMaximumSize(popupInputField.getPreferredSize());
		popupInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = contribution.GetInputForTextField();
				keyboardInput.show(popupInputField, contribution.GetCallbackForTextField());
			}
		});
//		box.add(new JLabel("Message: "));
//		box.add(CreateSpace(10, 0));
//		box.add(popupInputField);

		return box;
	}

	private Box CreateStartStopButtons(final CommsInstallationContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		startButton = new JButton("Start Daemon");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.OnStartClick();
			}
		});
		box.add(startButton);

		box.add(CreateSpace(10, 0));

		stopButton = new JButton("Stop Daemon");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.OnStopClick();
			}
		});
		box.add(stopButton);

		return box;
	}
	
	private Box CreateSendButton(final CommsInstallationContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		sendButton = new JButton("send");
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				contribution.OnSendClick();		
			}
		});
		
//		box.add(sendButton);
		return box;
	}
	
	private Box CreateStatus() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		statusLabel = new JLabel("Daemon Status");
		box.add(statusLabel);
		return box;
	}

	public void SetPopupText(String t) {
		popupInputField.setText(t);
	}

	public void SetStartButtonState(boolean state) {
		startButton.setEnabled(state);
	}

	public void SetStopButtonState(boolean state) {
		stopButton.setEnabled(state);
	}

	public void SetStatus(String text) {
		statusLabel.setText(text);
	}
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}
	
}
