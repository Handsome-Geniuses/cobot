package com.handsome.nosnhoj.impl.LockLED.ProgramNode;

import java.util.Arrays;

import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
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
	
	
	//Get from model functions
	private int GetZone() {
		return model.get(KEY_ZONE, DEF_ZONE);
	}
	private int[] GetColors() {
		return model.get(KEY_COLOR, DEF_VALUES);
	}
	private int[] GetDurations() {
		return model.get(KEY_DURATION, DEF_DURATION);
	}
	private boolean GetFlash() {
		return model.get(KEY_FLASH, DEF_FLASH);
	}
	
	//record changes and setting model
	public void OnZoneSelect(final int z) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(KEY_ZONE, z);
			}
		});
	}
	public void OnColorChange(final int[] rgb) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(KEY_COLOR, rgb);
			}
		});
	}
	public void OnDurationChange(final int[] dur) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(KEY_DURATION, dur);
			}
		});
	}
	public void OnFlashChange(final boolean f) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(KEY_FLASH, f);
			}
		});
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
//		
//		//only changing the getTitle on closing. I felt it was a bit laggy constantly updating? probably imagination
//		int colors[]=GetColors();
//		String s = "led(Z"+Integer.toString(GetZone()+1)+"L"+Integer.toString(colors[0])+Integer.toString(colors[1])+Integer.toString(colors[2]);
//		if(GetFlash()) {
//			s=s+"f)";
//		}
//		else {
//			s=s+")";
//		}
//		title=s;
	}

	@Override
	public String getTitle() {
		return "led("+Arrays.toString(GetColors()).replaceAll("\\s", "")+(GetFlash()==true?"f)":")");
	}

	@Override
	public boolean isDefined() {
		return true;
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
			writer.appendLine(installationContribution.GetFlashDurationOnVariable()+"="+Float.toString((float)(dur[0])/1000));
			writer.appendLine(installationContribution.GetFlashDurationOffVariable()+"="+Float.toString((float)(dur[1])/1000));
			writer.appendLine(installationContribution.GetFlashEnableVariable()+"=True");
		}
	}

}
