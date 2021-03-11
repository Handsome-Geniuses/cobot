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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.handsome.nosnhoj.impl.Comms.Daemon.CommsXmlRpc;
import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.handsome.nosnhoj.impl.Paint.paintconfig;
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
    private JSlider speed_slider;
    private JLabel  speed_label;
    
    private JButton btn_init;
    private JButton btn_laser;
    private JButton btn_home;
    
    private JPanel p;
	
    /*========================================================================================
     * Constructor and buildui
     * ======================================================================================*/
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
	
	/*========================================================================================
     * UI components
     * ======================================================================================*/
	private JPanel CreateInitPage(final JPanel panel) {
		JPanel page_init = new JPanel();
		page_init.setLayout(new BoxLayout(page_init, BoxLayout.Y_AXIS)); 
		page_init.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel label = new JLabel("Paint gun not initialized");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn_init = new JButton("Reference Home");
		btn_init.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_init.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String msg = "~";
				try {
					System.out.println(xml.PortDump());
					if(xml.SendMessage("h;").contains("not")) {
						return;
					}
//					JOptionPane.showMessageDialog(null, "asdasdsadasd");
					long time_Start = System.currentTimeMillis();
					while(msg.contains("~")) {
//						Thread.sleep(100);
						msg = xml.PortRead();
						if(System.currentTimeMillis()-time_Start>6666) {
							JOptionPane.showMessageDialog(null, "ERROR: Timedout. No reply.", "ERROR!", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					degs.setText(Integer.toString(0));
					//change to main layout
					CardLayout card = (CardLayout)(panel.getLayout());
					card.show(panel, PAGE_MAIN);

				} 
				catch (Exception e2) {
					System.out.println("Could not send go home message.");
				}
			}
		});
		
		
		page_init.add(label);
		page_init.add(btn_init);
		return page_init;
	}

	private JPanel CreateMainPage() {
		JPanel page_main = new JPanel();
		page_main.setLayout(new BoxLayout(page_main, BoxLayout.Y_AXIS)); 
		page_main.setAlignmentX(Component.CENTER_ALIGNMENT);
		page_main.add(CreateTitle("Paint Control"));
		page_main.add(CreateSpace(0, 10));
		page_main.add(CreateDegreeControl());
		page_main.add(CreateSpace(0, 25));
		page_main.add(CreateSpeedSlider());
		page_main.add(CreateSpace(0, 25));
		page_main.add(CreateMainButtons());
		
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
	private KeyboardNumberInput<Integer> GetKeyInput(){
		KeyboardNumberInput<Integer> in = numInput.createIntegerKeypadInput();
		in.setInitialValue(Integer.parseInt(degs.getText()));
		return in;
	}
	
	//handles numpad input
	private KeyboardInputCallback<Integer> GetInputCallback(){
		return new KeyboardInputCallback<Integer>() {

			@Override
			public void onOk(Integer value) {
				if (value>=paintconfig.max_ang) value = paintconfig.max_ang;
				else if (value<=-paintconfig.max_ang) value = -paintconfig.max_ang;
				degs.setText(Integer.toString(value));
				try {
					System.out.println(xml.PortDump());
					xml.SendMessage("s"+Integer.toString(speed_slider.getValue())+";");
					xml.SendMessage("p"+Integer.toString(value)+";");
				} catch (Exception e) {
					System.out.println("Failed to send on keyboard.");
				}
				
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
				Integer curr = Integer.parseInt(degs.getText()) + inc;
				if (curr>=max_ang) curr = (Integer) max_ang;
				degs.setText(Integer.toString(curr));
				try {
					xml.SendMessage("s"+Integer.toString(speed_slider.getValue())+";");
					xml.SendMessage("p"+Integer.toString(curr)+";");
				} catch (Exception e) {
					System.out.println("Failed to send on +.");
				}
			}
		});

        btn_down = new JButton(new ImageIcon(getClass().getResource("/icons/minus50x50.png")));
        btn_down.setFocusable(false);
        btn_down.setPreferredSize(new Dimension(50, 50));
        btn_down.setMaximumSize(btn_down.getPreferredSize());
        
        btn_down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer curr = Integer.parseInt(degs.getText()) - inc;
				if (curr>=max_ang) curr = (Integer) max_ang;
				degs.setText(Integer.toString(curr));
				try {
					xml.SendMessage("s"+Integer.toString(speed_slider.getValue())+";");
					xml.SendMessage("p"+Integer.toString(curr)+";");
				} catch (Exception e) {
					System.out.println("Failed to send on -.");
				}
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
        		KeyboardNumberInput<Integer> in = GetKeyInput();
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
    
    
    private Box CreateMainButtons() {
    	Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn_laser = new JButton("Laser");
		btn_laser.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn_laser.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					xml.SendMessage("l;");
				}
				catch (Exception e1) {
					System.out.println("Coult not toggle laser?");
				}
			}
		});
		
		btn_home = new JButton("Home");
		btn_home.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn_home.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String msg = "~";
				try {
					System.out.println(xml.PortDump());
					if(xml.SendMessage("h;").contains("not")) {
						return;
					}					
					long time_Start = System.currentTimeMillis();
					while(msg.contains("~")) {
						msg = xml.PortRead();
						if(System.currentTimeMillis()-time_Start>6666) {
							JOptionPane.showMessageDialog(null, "ERROR: Timedout. No reply.", "ERROR!", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					degs.setText(Integer.toString(0));

				} 
				catch (Exception e2) {
					System.out.println("Could not send go home message.");
				}
			}
		});
		
		box.add(btn_home);
		box.add(CreateSpace(10, 0));
		box.add(btn_laser);
		return box;	
    }

	private Component CreateSpace(int w, int h) {
		return Box.createRigidArea(new Dimension(w, h));
	}

	/*========================================================================================
     * Main overides
     * ======================================================================================*/
	@Override
	public void openView() {
		//check if gun is initialized. then decide view.
		try {
			CardLayout card = (CardLayout)(p.getLayout());
			System.out.println(xml.PortDump());	//dump messages before send
			String msg = xml.SendMessage("d;");
			System.out.println(msg);
			Thread.sleep(100);	//allow to buffer up //or while loop ~.
			msg = xml.PortRead();	//now read
			System.out.println("read: " + msg);
			
			if(msg.contains("true")) {
				System.out.println(xml.PortDump());
				System.out.println(xml.SendMessage("g;"));
				Thread.sleep(100);
				String d = xml.PortRead().replaceAll("\\s","");
				System.out.println("degs: "+d);
				degs.setText(d);
				card.show(p, PAGE_MAIN);
			}
			else {
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
