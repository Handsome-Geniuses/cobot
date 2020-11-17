package com.handsome.nosnhoj.impl.LockLED;

import com.handsome.nosnhoj.impl.Comms.Installation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

public class LockLEDProgramContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private final LockLEDProgramView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	private static final String KEY_ZONE = "zones";
	private static final int DEF_ZONE = 0;	//0 is zone 1
	
	private static final String KEY_COLOR = "led_colors";
	private static final int[] DEF_VALUES = {0,0,0};
	
	public LockLEDProgramContribution(ProgramAPIProvider apiProvider, LockLEDProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
	}
	private CommsInstallationContribution GetCommsInstallationContribution() {
		return apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class);
	}
	
	
	private void SetZone(final int z) {
		model.set(KEY_ZONE, z);
	}
	private int GetZone() {
		return model.get(KEY_ZONE, DEF_ZONE);
	}
	public void OnZoneSelect(final int z) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				SetZone(z);
			}
		});
	}
	
	private void SetColors(int r, int g, int b) {
		int temp[] = {r, g, b};
		model.set(KEY_COLOR, temp);
	}
	private int[] GetColors() {
		return model.get(KEY_COLOR, DEF_VALUES);
	}
	public void OnColorChange(final int r, final int g, final int b) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				SetColors(r, g, b);
			}
		});
	}
	
	@Override
	public void openView() {
		view.SetZone(GetZone());
		view.SetSliders(GetColors());
//		int temp[] = GetColors();
//		view.SetSliders(temp[0], temp[1], temp[2]);
	}

	@Override
	public void closeView() {
		model.set(KEY_COLOR, view.GetSliderVals());
	}

	@Override
	public String getTitle() {
		// TODO somehow translate sliders to short text
		return "led()"; //for now, generic.
	}

	@Override
	public boolean isDefined() {
		return view.isZoneSelected();
//		return true; //true for now
	}

	@Override
	public void generateScript(ScriptWriter writer) {
//		writer.assign("remove_this_later", "1");
//		"\""+askdlasdjklsd()+"\""
		String temp = ".send_message(";
		int colors[]=GetColors();
		temp = temp+"\"@Z"+Integer.toString(GetZone()+1)+";L";
		temp = temp+Integer.toString(colors[0])+Integer.toString(colors[1])+Integer.toString(colors[2])+";\")";
		writer.appendLine(GetCommsInstallationContribution().GetXmlRpcVariable()+temp);
	}

}
