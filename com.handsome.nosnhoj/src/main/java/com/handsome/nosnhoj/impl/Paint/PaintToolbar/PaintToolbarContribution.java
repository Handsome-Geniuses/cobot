package com.handsome.nosnhoj.impl.Paint.PaintToolbar;

import java.awt.CardLayout;
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

import org.apache.xmlrpc.XmlRpcException;

import com.handsome.nosnhoj.impl.Comms.Daemon.CommsXmlRpc;
import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;



public class PaintToolbarContribution implements SwingToolbarContribution{
	final static String PAGE_INIT = "INIT";
	final static String PAGE_MAIN = "MAIN";
	
	private static final int inc = 5;
	private static final int max_ang = 120;
	private final ToolbarContext context;
	private final CommsXmlRpc xml;

    private JButton btn_up;
    private JButton btn_down;
    private JTextField degs;
    private KeyboardInputFactory numInput;
    
    private JButton btn_home;
    
    private JPanel p;
	
	public PaintToolbarContribution(ToolbarContext context) {
		this.context = context;
		numInput = context.getAPIProvider().getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		xml = context.getAPIProvider().getApplicationAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc();
	}
	
	@Override
	public void buildUI(JPanel panel) {		
		p = panel;
		p.setLayout(new CardLayout());
		
		p.add(CreateInitPage(p), PAGE_INIT);
		p.add(CreateMainPage(), PAGE_MAIN);
		
	}
	
	private JPanel CreateInitPage(final JPanel panel) {
		JPanel page_init = new JPanel();
		page_init.setLayout(new BoxLayout(page_init, BoxLayout.Y_AXIS)); 
		page_init.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel label = new JLabel("Paint gun not initialized");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn_home = new JButton("Reference Home");
		btn_home.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_home.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String msg = "~";
				try {
					xml.PortDump();
//					while(!xml.PortRead().contains("~")); //dump
					if(xml.SendMessage("h;").contains("not")) {
						return;
					}
					while(msg.contains("~")) {
						Thread.sleep(100);
						msg = xml.PortRead();
					}
					//change to main layout
					CardLayout card = (CardLayout)(panel.getLayout());
					card.show(panel, PAGE_MAIN);
//					if(msg.contains("done")) {
//						
//					}
				} 
				catch (Exception e2) {
					System.out.println("Could not send go home message.");
				}
			}
		});
		
		
		page_init.add(label);
		page_init.add(btn_home);
		return page_init;
	}

	private JPanel CreateMainPage() {
		JPanel page_main = new JPanel();
		page_main.setLayout(new BoxLayout(page_main, BoxLayout.Y_AXIS)); 
		page_main.setAlignmentX(Component.CENTER_ALIGNMENT);
		page_main.add(CreateTitle("Paint Control"));
		page_main.add(CreateSpace(0, 10));
		page_main.add(CreateDegreeControl());
		
		return page_main;
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
				Double curr = Double.parseDouble(degs.getText())+inc;
				if (curr>=max_ang) curr = (double) max_ang;
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
				Double curr = Double.parseDouble(degs.getText())-inc;
				if (curr<=-max_ang) curr = (double)-max_ang;
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
		//dump all messages
		try {
			xml.PortDump();
//			while(!xml.PortRead().contains("~"));
			
		} 
		catch (Exception e) {
			System.out.println(">>>>> Could not dump!");
		}
		//check if gun is initialized. then decide view.
		try {
			CardLayout card = (CardLayout)(p.getLayout());
			String msg = xml.SendMessage("d;");
			System.out.println("dump: " + msg);
			Thread.sleep(100);	//allow to buffer up
			msg = xml.PortRead();
			System.out.println("read: " + msg);
			if(msg.contains("true")) {
				System.out.println("yes");
				card.show(p, PAGE_MAIN);
			}
			else {
				System.out.println("no");
				card.show(p, PAGE_INIT);
			}
		} 
		catch (Exception e) {
			System.out.println(">>>>> failed to check if init");
		}
		
	}

	@Override
	public void closeView() {
	}
	
}
