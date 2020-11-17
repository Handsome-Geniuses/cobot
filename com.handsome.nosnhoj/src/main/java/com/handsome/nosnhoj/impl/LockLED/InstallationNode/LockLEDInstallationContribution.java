package com.handsome.nosnhoj.impl.LockLED.InstallationNode;

import com.handsome.nosnhoj.impl.Comms.Installation.CommsInstallationContribution;
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
		

		this.g_FlashStringVar = new GlobalVariable() {
			
			@Override
			public Type getType() {
				return Type.GLOBAL;
			}
			
			@Override
			public String getDisplayName() {
				return "flash_string_command";
			}
		};
		this.g_FlashEnableVar = new GlobalVariable() {
			
			@Override
			public Type getType() {
				return Type.GLOBAL;
			}
			
			@Override
			public String getDisplayName() {
				return "flash_enable";
			}
		};
		this.g_OnDurationVar = new GlobalVariable() {
			
			@Override
			public Type getType() {
				return Type.GLOBAL;
			}
			
			@Override
			public String getDisplayName() {
				return "flash_duration_on";
			}
		};
		this.g_OffDurationVar = new GlobalVariable() {
			
			@Override
			public Type getType() {
				return Type.GLOBAL;
			}
			
			@Override
			public String getDisplayName() {
				return "flash_duration_off";
			}
		};
	}
	
	public String GetFlashStringVariable() {
		return g_FlashStringVar.getDisplayName();
	}
	public String GetFlashEnableVariable() {
		return g_FlashEnableVar.getDisplayName();
	}
	public String GetFlashDurationOn() {
		return g_OnDurationVar.getDisplayName();
	}
	public String GetFlashDurationOff() {
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
		//defaults. hopefully they can be overwritten
		writer.appendLine(GetFlashEnableVariable()+"=False");
		writer.appendLine(GetFlashStringVariable()+"=\"$Hello There\"");
		writer.appendLine(GetFlashDurationOn()+"=0.5");
		writer.appendLine(GetFlashDurationOff()+"=0.5");
		//thread to handle led
		writer.appendLine("thread "+ThreadFunctionName+"():");
			writer.appendLine("while(True):");
				writer.appendLine("if("+GetFlashEnableVariable()+"):");
					writer.appendLine(comms.GetXmlRpcVariable()+".send_message("+GetFlashStringVariable()+")");
					writer.appendLine("sleep("+g_OnDurationVar.getDisplayName()+")");
					writer.appendLine(comms.GetXmlRpcVariable()+".send_message(\"@X;\")");
					writer.appendLine("sleep("+g_OffDurationVar.getDisplayName()+")");
//					writer.appendLine("set_standard_digital_out(1, not get_standard_digital_out(1))");
//					writer.appendLine("sleep(1)");
				writer.appendLine("end");
			writer.appendLine("end");
		writer.appendLine("end");
		writer.appendLine("run "+ThreadFunctionName+"()");
	}

}
