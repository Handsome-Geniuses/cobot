package com.handsome.nosnhoj.impl.LockLED.InstallationNode;

import com.handsome.nosnhoj.impl.util.GV;
import com.handsome.nosnhoj.impl.util;
import com.handsome.nosnhoj.impl.Comms.DaemonInstallation.CommsInstallationContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.variable.GlobalVariable;

public class LockLEDInstallationContribution implements InstallationNodeContribution{

	private final CommsInstallationContribution comms;
	
	private final InstallationAPIProvider apiProvider;
	private final LockLEDInstallationView view;
	private final DataModel model;
	
	private final GlobalVariable g_FlashStringVar;
	private final GlobalVariable g_FlashEnableVar;
	private final GlobalVariable g_OnDurationVar;
	private final GlobalVariable g_OffDurationVar;
	
	private static final String ThreadFunctionName = "Thread_Flash";
//	private static final String ThreadHandler = "Thread_Flash_Handler";
	
	public LockLEDInstallationContribution(InstallationAPIProvider apiProvider,
			LockLEDInstallationView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		
		this.comms = apiProvider.getInstallationAPI().getInstallationNode(CommsInstallationContribution.class);
		
		this.g_FlashStringVar = new GV("flash_string_command");
		this.g_FlashEnableVar = new GV("flash_enable");
		this.g_OnDurationVar = new GV("flash_duration_on");
		this.g_OffDurationVar = new GV("flash_duration_off");
		
		
		util.AddScriptFunction(apiProvider, "mypopup", "arg");
	}
	
	public String GetFlashStringVariable() {
		return g_FlashStringVar.getDisplayName();
	}
	public String GetFlashEnableVariable() {
		return g_FlashEnableVar.getDisplayName();
	}
	public String GetFlashDurationOnVariable() {
		return g_OnDurationVar.getDisplayName();
	}
	public String GetFlashDurationOffVariable() {
		return g_OffDurationVar.getDisplayName();
	}
	
	@Override
	public void openView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		//global variable initializing
		writer.appendLine(GetFlashEnableVariable()+"=False");
		writer.appendLine(GetFlashStringVariable()+"=\"$Hello There\"");
		writer.appendLine(GetFlashDurationOnVariable()+"=0.5");
		writer.appendLine(GetFlashDurationOffVariable()+"=0.5");
		
		
		//thread to handle led
		writer.appendLine("thread "+ThreadFunctionName+"():");
			writer.appendLine("while(True):");
				writer.appendLine("if("+GetFlashEnableVariable()+"):");
					writer.appendLine(comms.GetXmlRpcVariable()+".send_message("+GetFlashStringVariable()+")");
					writer.appendLine("sleep("+g_OnDurationVar.getDisplayName()+")");
					writer.appendLine(comms.GetXmlRpcVariable()+".send_message(\"@X;\")");
					writer.appendLine("sleep("+g_OffDurationVar.getDisplayName()+")");
				writer.appendLine("end");
			writer.appendLine("end");
		writer.appendLine("end");
		writer.appendLine("run "+ThreadFunctionName+"()");
		
		writer.appendLine("def mypopup(s):");
		writer.appendLine("popup(s, blocking=True)");
		writer.appendLine("end");
	}
	
	
	
}
