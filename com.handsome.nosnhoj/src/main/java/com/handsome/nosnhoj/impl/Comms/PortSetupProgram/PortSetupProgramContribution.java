package com.handsome.nosnhoj.impl.Comms.PortSetupProgram;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
public class PortSetupProgramContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private final PortSetupProgramView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	private static final String KEY_PORT = "port_select";
	private static final String DEF_PORT = "/dev/ttyTool"; //TODO will need to update this to ttyTool later
	
	private static final String KEY_BAUD = "baud_rate";
	private static final Integer DEF_BAUD = 9600;
	
	public PortSetupProgramContribution(ProgramAPIProvider apiProvider, PortSetupProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
	}
	
	private CommsInstallationContribution GetInstallationContribution() {
		return apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class);
	}
	
	
	private void SetPort(final String port) {
		model.set(KEY_PORT, port);
	}
	
	private String GetPort() {
		return model.get(KEY_PORT, DEF_PORT);
	}
	
	public void OnPortSelect(final String port) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				SetPort(port);
				view.SetPortSelection(port);
			}
		});
	}
	
	private void SetBaud(final Integer rate) {
		model.set(KEY_BAUD, rate);
	}
	private Integer GetBaud() {
		return model.get(KEY_BAUD, DEF_BAUD);
		
	}
	public void OnBaudSelect(final Integer rate) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				SetBaud(rate);
				view.SetBaudRate(rate);
			}
		});
	}
	
	@Override
	public void openView() {
		try {
			String portString = GetInstallationContribution().GetXmlRpc().GetPorts();
			String[] portArray = null;
			portArray = portString.split(",");
			view.SetPortItems(portArray);
			view.SetBaudRate(GetBaud());
//			view.SetPortItems(GetInstallationContribution().GetXmlRpc().GetPorts());
		} 
		catch (Exception e) {
			System.out.println("Could not set get available ports.");
		}
	}

	@Override
	public void closeView() {
		
	}

	@Override
	public String getTitle() {
		return "open("+GetPort().substring(8)+")";
	}

	@Override
	public boolean isDefined() {
		return GetInstallationContribution().isDefined();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine(GetInstallationContribution().GetXmlRpcVariable()+".port_open(\""+GetPort()+"\","+GetBaud()+")");
	}

}
