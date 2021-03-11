package com.handsome.nosnhoj.impl.Comms.DaemonInstallation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommsInstallationView implements SwingInstallationNodeView<CommsInstallationContribution> {

	private final ViewAPIProvider apiProvider;
	
	private JTextField popupInputField;
	private JButton startButton;
	private JButton stopButton;
	private JLabel statusLabel;
	
	/*========================================================================================
     * Constructor and Build
     * ======================================================================================*/
	public CommsInstallationView(ViewAPIProvider apiProvider) {
		this.apiProvider=apiProvider;
	}

	@Override
	public void buildUI(JPanel panel, CommsInstallationContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//		panel.add(CreateTitle("Daemon status:"));
		panel.add(CreateStatus());
		panel.add(CreateSpace(0, 10));
		panel.add(CreateStartStopButtons(contribution));
		panel.add(CreateSpace(0, 30));
//		panel.add(CreateTitle("Function Descriptions:"));
		panel.add(CreateSpace(0, 10));
		
	}
	
	/*========================================================================================
     * Get and sets
     * ======================================================================================*/

	public void SetStartButtonState(boolean state) {
		startButton.setEnabled(state);
	}

	public void SetStopButtonState(boolean state) {
		stopButton.setEnabled(state);
	}

	public void SetStatus(String text) {
		statusLabel.setText(text);
	}
	
	/*========================================================================================
     * UI Components
     * ======================================================================================*/
	private Box CreateTitle(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(s);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 24));
		box.add(label);
		
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
	
	private Box CreateStatus() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		statusLabel = new JLabel("Daemon Status");
		box.add(statusLabel);
		return box;
	}

	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}
	
}
