package com.handsome.nosnhoj.impl.Comms.CommsToolBar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class CommsToolbarContribution implements SwingToolbarContribution {
	
	private final ToolbarContext context;
	private JComboBox<String> portsBox = new JComboBox<String>();
	private JComboBox<Integer> baudBox = new JComboBox<Integer>();
	
	private static final Integer[] baudrates= {4800, 9600, 115200};
	
	private String PortSelected =  "/dev/ttyTool";
	
	
	private JButton btn_Send;
	private JButton btn_Connect;
	private JButton btn_Disconnect;
	
	private KeyboardInputFactory kbInput;
	
	private JTextField UserInput;
	
	
	
	public CommsToolbarContribution(ToolbarContext context) {
		this.context = context;
		kbInput = context.getAPIProvider().getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
	}
	
	@Override
	public void buildUI(JPanel panel) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(CreateTitle("Comms Setup"));
		panel.add(CreateNodeInfo("Select port and baudrate to open."));
		panel.add(CreateSpace(0,20));
		panel.add(CreatePortComboBox());
		panel.add(CreateSpace(0,20));
		panel.add(CreateBaudComboBox());
		panel.add(CreateSpace(0,20));
		panel.add(CreatePortButtons());
		panel.add(CreateSpace(0, 30));
		panel.add(CreateTitle("Send Message"));
		panel.add(CreateNodeInfo("Type message and hit send."));
		panel.add(CreateSpace(0,10));
		panel.add(CreateInput());
		panel.add(CreateSpace(0,20));
		panel.add(CreateSendButton());
	}
	
	private Box CreateNodeInfo(String s) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(new JLabel(s));
		return box;
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
	
	public void SetPortItems() {
		try {
			String portString = context.getAPIProvider().getApplicationAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc().GetPorts();
			String[] portArray = null;
			portArray = portString.split(",");
			portsBox.removeAllItems();
			portsBox.setModel(new DefaultComboBoxModel<String>(portArray));
			portsBox.addItem("/dev/ttyTool");
			portsBox.setSelectedItem(PortSelected);
		} 
		catch (Exception e) {
			portsBox.removeAllItems();
			System.out.println("Could not set get available ports.");
		}
	}
	
	private Box CreatePortComboBox() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		portsBox.setPreferredSize(new Dimension(200, 30));
		portsBox.setMaximumSize(portsBox.getPreferredSize());
		SetPortItems();

		portsBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					//at
				}
			}
		});
		
		box.add(new JLabel("Ports: "));
		box.add(CreateSpace(10, 0));
		box.add(portsBox);
		
		return box;
	}
	
	private void SetBaudItems() {
		baudBox.removeAllItems();
		baudBox.setModel(new DefaultComboBoxModel<Integer>(baudrates));
	}
	
	private Box CreateBaudComboBox() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		baudBox.setPreferredSize(new Dimension(110, 30));
		baudBox.setMaximumSize(portsBox.getPreferredSize());
		SetBaudItems();
		
		baudBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
				}
			}
		});
		
		
		box.add(new JLabel("Baudrate: "));
		box.add(CreateSpace(10, 0));
		box.add(baudBox);
		
		return box;
	}
	
	private Box CreatePortButtons() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		btn_Connect = new JButton("Open");
		btn_Connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					context.getAPIProvider().getApplicationAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc().PortOpen(portsBox.getSelectedItem().toString(), baudBox.getSelectedItem().toString());
				} 
				catch (Exception e) {
					System.out.println(">>>>> Failed to connect");
				}
			}
		});
		
		btn_Disconnect = new JButton("Close");
		btn_Disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					context.getAPIProvider().getApplicationAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc().PortClose();
				} 
				catch (Exception e) {
					System.out.println(">>>>> Failed to close.");
				}
			}
		});
		
		box.add(btn_Connect);
		box.add(CreateSpace(20, 0));
		box.add(btn_Disconnect);
		
		return box;
	}
	
	private Box CreateSendButton() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		btn_Send = new JButton("Send");
		btn_Send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					context.getAPIProvider().getApplicationAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc().SendMessage(UserInput.getText());
				} 
				catch (Exception e) {
					System.out.println(">>>>> Failed to send.");
				}
			}
		});
		
		box.add(btn_Send);
		return box;
	}
	
	private KeyboardTextInput GetKeyInput() {
		KeyboardTextInput in = kbInput.createStringKeyboardInput();
		in.setInitialValue("");
		return in;
	}
	
	private KeyboardInputCallback<String> GetInputCallback (){
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				UserInput.setText(value);
				
			}
		};
	}
	
	private Box CreateInput() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		UserInput = new JTextField();
		UserInput.setFocusable(false);
		UserInput.setPreferredSize(new Dimension(230, 30));
		UserInput.setMaximumSize(UserInput.getPreferredSize());
		
		UserInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput in = GetKeyInput();
				in.show(UserInput, GetInputCallback());
			}
		});
		
		box.add(new JLabel("Message: "));
		box.add(CreateSpace(10,0 ));
		box.add(UserInput);
		return box;
	}
	
	
	
	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}

	@Override
	public void openView() {
		SetPortItems();
		
	}

	@Override
	public void closeView() {
		PortSelected = (String) portsBox.getSelectedItem();
	}

}
