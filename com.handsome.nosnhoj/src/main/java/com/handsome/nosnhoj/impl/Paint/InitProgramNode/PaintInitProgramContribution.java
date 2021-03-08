package com.handsome.nosnhoj.impl.Paint.InitProgramNode;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

public class PaintInitProgramContribution implements ProgramNodeContribution{
	private final ProgramAPIProvider apiProvider;
	private final PaintInitProgramView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	private final String xml_var;
	
	private static final String KEY_ACTIVATE_ONLY = "activate_only";
	private static final Boolean DEF_ACTIVATE_ONLY = false;
	
	
	/*========================================================================================
     * Constructor
     * ======================================================================================*/
	public PaintInitProgramContribution(ProgramAPIProvider apiProvider, PaintInitProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
		xml_var = apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpcVariable();
	}
	
	/*========================================================================================
     * Get and sets
     * ======================================================================================*/
	private void SetCheck(Boolean in) {
		model.set(KEY_ACTIVATE_ONLY, in);
	}
	
	private Boolean GetCheck() {
		return model.get(KEY_ACTIVATE_ONLY, DEF_ACTIVATE_ONLY);
	}
	/*========================================================================================
     * Checkbox listener
     * ======================================================================================*/
	public void OnCheck(final Boolean b) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				SetCheck(b);
			}
		});
	}
	
	/*========================================================================================
     * Script helpers
     * ======================================================================================*/
	private String SendString(String s) {
		return xml_var+".send_message(\""+s+"\")";
	}
	
	private String GetDumpString() {
		return xml_var+".msg_dump()";
	}
	/*========================================================================================
     * Main Overrides
     * ======================================================================================*/
	@Override
	public void openView() {
		view.SetCheck(GetCheck());
		
	}
	@Override
	public void closeView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getTitle() {
		return "Paint Activate";
	}
	@Override
	public boolean isDefined() {
		boolean connected = false;
		try {
			connected = apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class).GetXmlRpc().IsPortConnected();
		}
		catch (Exception e) {
			System.out.println("!!! paint node define failure.");
		}
		return connected;
	}
	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine(GetDumpString());
		if(GetCheck()) {
			writer.appendLine(SendString("c;"));
			writer.appendLine("sleep(0.100)");
			writer.appendLine("while not "+xml_var+".string_contains("+xml_var+".get_message(), \"!c\"):");
			writer.appendLine("sleep(0.1)");
			writer.appendLine("end");
		}
		else {
			writer.appendLine(SendString("h;"));
			writer.appendLine("sleep(0.100)");
			writer.appendLine("while not "+xml_var+".string_contains("+xml_var+".get_message(), \"!h\"):");
			writer.appendLine("sleep(0.1)");
			writer.appendLine("end");
		}
		
		
		
	}
	
}
