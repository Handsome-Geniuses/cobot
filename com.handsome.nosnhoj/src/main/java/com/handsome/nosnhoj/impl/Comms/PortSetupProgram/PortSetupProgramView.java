package com.handsome.nosnhoj.impl.Comms.PortSetupProgram;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class PortSetupProgramView implements SwingProgramNodeView<PortSetupProgramContribution>{
	private static final int gap_sm = 10;
	private static final int gap_md = 25;
	private static final int gap_lg = 50; 
	
	private static final Integer[] baudrates= {4800, 9600, 19200, 38400, 57600, 115200};
	
	private final ViewAPIProvider apiProvider;
	
//	private JTextField UserInput;
//	private JButton SendButton;
	private JComboBox<String> portsBox = new JComboBox<String>();
	private JComboBox<Integer> baudBox = new JComboBox<Integer>();
	
	
	public PortSetupProgramView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<PortSetupProgramContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(CreateNodeInfo());
		panel.add(CreateSpace(0, gap_md));
		panel.add(CreatePortComboBox(provider));
		panel.add(CreateSpace(0, gap_md));
		panel.add(CreateBaudComboBox(provider));
	}
	
	
	public void SetPortItems(String[] ports) {
		portsBox.removeAllItems();
		portsBox.setModel(new DefaultComboBoxModel<String>(ports));
		portsBox.addItem("/dev/ttyTool");
	}
	public void SetPortSelection(String port) {
		portsBox.setSelectedItem(port);
	}
	
	public void SetBaudItems() {
		baudBox.removeAllItems();
		baudBox.setModel(new DefaultComboBoxModel<Integer>(baudrates));
	}
	public void SetBaudRate(Integer rate) {
		baudBox.setSelectedItem(rate);
	}
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}
	
	private Box CreateNodeInfo() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel("Select port and baudrate to open."));
		return box;
	}
	
	private Box CreatePortComboBox(final ContributionProvider<PortSetupProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		portsBox.setPreferredSize(new Dimension(200, 30));
		portsBox.setMaximumSize(portsBox.getPreferredSize());
		
		portsBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					provider.get().OnPortSelect((String)arg0.getItem());
				}
			}
		});
		
		box.add(new JLabel("Ports: "));
		box.add(CreateSpace(gap_sm, 0));
		box.add(portsBox);
		
		return box;
	}
	
	private Box CreateBaudComboBox(final ContributionProvider<PortSetupProgramContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		baudBox.setPreferredSize(new Dimension(110, 30));
		baudBox.setMaximumSize(portsBox.getPreferredSize());
		SetBaudItems();
		
		baudBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					provider.get().OnBaudSelect((Integer)arg0.getItem());
				}
			}
		});
		
		
		box.add(new JLabel("Baudrate: "));
		box.add(CreateSpace(gap_sm, 0));
		box.add(baudBox);
		
		return box;
	}
	
//	private Box CreateInput(final ContributionProvider<PortSetupProgramContribution> provider) {
//		Box box = Box.createHorizontalBox();
//		box.setAlignmentX(Component.LEFT_ALIGNMENT);
//		
//		UserInput = new JTextField();
//		UserInput.setFocusable(false);
//		UserInput.setPreferredSize(new Dimension(230, 30));
//		UserInput.setMaximumSize(UserInput.getPreferredSize());
//		UserInput.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				KeyboardTextInput keyboardTextInput = provider.get().GetInputTextField();
//				keyboardTextInput.show(UserInput, provider.get().GetInputCallback());
//			}
//		});
//		
//		box.add(new JLabel("Message: "));
//		box.add(CreateSpace(gap_sm, 0));
//		box.add(UserInput);
//		
//		return box;
//	}

	
//	private Box CreateSendButton(final ContributionProvider<PortSetupProgramContribution> provider) {
//		Box box = Box.createHorizontalBox();
//		box.setAlignmentX(Component.LEFT_ALIGNMENT);
//		
//		SendButton = new JButton("send");
//		SendButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				provider.get().OnSendClick();
//			}
//		});
//		
//		box.add(SendButton);
//		return box;
//	}
	
}
