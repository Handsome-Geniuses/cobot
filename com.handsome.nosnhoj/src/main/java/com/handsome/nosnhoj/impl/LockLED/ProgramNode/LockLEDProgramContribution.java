package com.handsome.nosnhoj.impl.LockLED.ProgramNode;

import com.handsome.nosnhoj.impl.Comms.Installation.CommsInstallationContribution;
import com.handsome.nosnhoj.impl.LockLED.InstallationNode.LockLEDInstallationContribution;
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
	
	private final LockLEDInstallationContribution installationContribution;
	
	private String title;
	
	private static final String KEY_ZONE = "zones";
	private static final int DEF_ZONE = 0;	//0 is zone 1
	
	private static final String KEY_COLOR = "led_colors";
	private static final int[] DEF_VALUES = {0,0,0};
	
	private static final String KEY_FLASH = "flash_enabled";
	private static final boolean DEF_FLASH = false;
	
	private static final String KEY_DURATION = "flash_duration";
	private static final int[] DEF_DURATION = {0, 0};
	
	public LockLEDProgramContribution(ProgramAPIProvider apiProvider, LockLEDProgramView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.installationContribution = apiProvider.getProgramAPI().getInstallationNode(LockLEDInstallationContribution.class);
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
		this.title = "led()";
		
	}
	private CommsInstallationContribution GetCommsInstallationContribution() {
		return apiProvider.getProgramAPI().getInstallationNode(CommsInstallationContribution.class);
	}
	
	
	//zone functions
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
	
	//RGB functions
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
	
	//flash functions
//	private void SetFlash(boolean f) {
//		model.set(KEY_FLASH, f);
//	}
	private boolean GetFlash() {
		return model.get(KEY_FLASH, DEF_FLASH);
	}
	
	//duration functions
//	private void SetDurations(int[] d) {
//		model.set(KEY_DURATION, d);
//	}
	private int[] GetDurations() {
		return model.get(KEY_DURATION, DEF_DURATION);
	}
	
	@Override
	public void openView() {
		view.SetZone(GetZone());
		view.SetRGBSliders(GetColors());
		view.SetFlashBox(GetFlash());
		view.SetDurationSliders(GetDurations());
	}

	@Override
	public void closeView() {
		model.set(KEY_COLOR, view.GetSliderRGB());
		model.set(KEY_FLASH, view.GetFlashStatus());
		model.set(KEY_DURATION, view.GetSliderDurations());
		
		int colors[]=GetColors();
		String s = "led(Z"+Integer.toString(GetZone()+1)+"L"+Integer.toString(colors[0])+Integer.toString(colors[1])+Integer.toString(colors[2]);
		if(GetFlash()) {
			s=s+"f)";
		}
		else {
			s=s+")";
		}
		title=s;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean isDefined() {
		return view.isZoneSelected();
//		return true; //true for now
	}

	private String GetRGBCommand() {
		int colors[] = GetColors();
		String command = "\"@Z"+Integer.toString(GetZone()+1)+";L";
		command = command+Integer.toString(colors[0])+Integer.toString(colors[1])+Integer.toString(colors[2])+";\"";
		return command;
	}
	@Override
	public void generateScript(ScriptWriter writer) {
//		writer.assign("remove_this_later", "1");
		String xmlrpc = GetCommsInstallationContribution().GetXmlRpcVariable();
		String rgbcommand = GetRGBCommand();
		writer.appendLine(installationContribution.GetFlashStringVariable()+"="+rgbcommand);
//		writer.appendLine(xmlrpc+".send_message("+installationContribution.GetFlashStringVariable()+")");
		if(!GetFlash()) {
			writer.appendLine(installationContribution.GetFlashEnableVariable()+"=False");
			writer.appendLine(xmlrpc+".send_message("+installationContribution.GetFlashStringVariable()+")");
		}
		else {
			int dur[] = GetDurations(); //0-2000ms. need convert to seconds.
			writer.appendLine(installationContribution.GetFlashDurationOn()+"="+Float.toString((float)(dur[0])/1000));
			writer.appendLine(installationContribution.GetFlashDurationOff()+"="+Float.toString((float)(dur[1])/1000));
			writer.appendLine(installationContribution.GetFlashEnableVariable()+"=True");
		}
	}

}
